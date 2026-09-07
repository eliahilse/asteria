import base64
import json
import subprocess
import tempfile
import unittest
from pathlib import Path
from research.extract_context import AST_SOURCE, render
from research.evaluate_security import find_jdk


class ContextTests(unittest.TestCase):
    def test_ast_ignores_comments_and_tracks_a_direct_read_to_allocation(self):
        jdk=find_jdk()
        with tempfile.TemporaryDirectory() as directory:
            temp=Path(directory);sources=temp/'sources';sources.mkdir();classes=temp/'classes';classes.mkdir()
            (sources/'Fixture.java').write_text('''class Fixture {
                // new URL("http://comment.example");
                String decoy = "new ObjectInputStream(x)";
                void load(java.io.DataInputStream input) throws Exception {
                    int count = input.readInt();
                    int[] values = new int[count];
                    java.net.URL endpoint = new java.net.URL("http://fixture.invalid");
                }
            }''')
            subprocess.run([str(jdk/'javac'),'-d',str(classes),str(AST_SOURCE)],capture_output=True,check=True)
            out=subprocess.check_output([str(jdk/'java'),'-cp',str(classes),'research.context.AstFacts',str(sources)],text=True)
            events=[[base64.b64decode(x).decode() for x in line.split('\t')] for line in out.splitlines()]
            self.assertFalse(any('comment.example' in e[4] for e in events))
            self.assertFalse(any(e[0]=='construct' and 'ObjectInputStream' in e[4] for e in events))
            flow=next(e for e in events if e[0]=='flow_candidate')
            self.assertIn('new int[count]',flow[4]);self.assertIn('input.readInt',flow[5])

    def test_render_obeys_type_and_scope_selection(self):
        source={'path':'fixture.java','line':1}
        report={'policy':{'threat_model':'editable file'},'facts':[
            {'id':'a','type':'C1','scope':'feature','status':'observed','text':'selected','source':source},
            {'id':'b','type':'C2','scope':'feature','status':'candidate','text':'wrong type','source':source},
            {'id':'c','type':'C1','scope':'repository','status':'observed','text':'wrong scope','source':source}]}
        text=render(report,['C1'],'feature')
        self.assertIn('selected',text);self.assertNotIn('wrong type',text);self.assertNotIn('wrong scope',text)
        self.assertIn('wrong scope',render(report,['C1'],'repository'))

if __name__=='__main__':unittest.main()
