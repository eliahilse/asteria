package apoDefence;

import java.io.*;
import javax.sound.sampled.*;

/**
 * Klasse, die Sounds einliest und abspielt
 * @author Egon Olsen aus dem Java-Forum.org Forum
 *
 */
public class ApoDefenceSounds
{

   private static Mixer mixer;
   private byte[] data;
   private AudioFormat format;
   private DataLine.Info dataLineInfo;
   private boolean ok=false;
   private Clip currentLoopClip=null;
   private Clip lastClip=null;
   private boolean mute=false;

   static {
      Mixer.Info[] mixers=AudioSystem.getMixerInfo();
      for (int i=0; i<mixers.length; i++) {
         if ("Java Sound Audio Engine".equals(mixers[i].getName())) {
            mixer=AudioSystem.getMixer(mixers[i]);
         }
      }
   }

   public static void main(String[] args) {
     ApoDefenceSounds s1=new ApoDefenceSounds(new File("sound1.wav"));
     ApoDefenceSounds s2=new ApoDefenceSounds(new File("sound2.wav"));
     ApoDefenceSounds s3=new ApoDefenceSounds(new File("sound3.wav"));
     
     for (int i=0; i<50; i++) {
       double rnd=Math.random();
       if (rnd<0.3) {
         s1.play();
       }
       else {
         if (rnd < 0.6) {
           s2.play();
         }
         else {
           if (rnd < 1) {
             s3.play();
           }
         }
       }
       
       try {
         Thread.sleep(200);
       } catch(Exception e) {
         //egal
       }
     }
     
     while (s1.isRunning()||s2.isRunning()||s3.isRunning()) {
       // Warten, bis auch der letzte fertig ist...
       Thread.yield();
     }
     
     System.exit(0);
   }

   public ApoDefenceSounds(File sound) {
      try {
         System.out.println("Loading sound from file");
         AudioInputStream soundStream=AudioSystem.getAudioInputStream(sound);
         format=soundStream.getFormat();

         int len=(int) (format.getFrameSize()*soundStream.getFrameLength());
         data=new byte[len];
         int l=0;
         int lp=0;
         do {
            l=soundStream.read(data, lp, len);
            lp+=l;
         } while (l!=-1);

         dataLineInfo=new DataLine.Info(Clip.class, format);
         if (!AudioSystem.isLineSupported(dataLineInfo)) {
            System.out.println("Audio-Line not supported!");
         }
      } catch (Exception e) {
         System.out.println("Error loading sound: "+e);
      }
   }

   public void setMute(boolean is) {
      mute=is;
   }

   public boolean playedFine() {
      return ok;
   }

   public boolean isRunning() {
      return lastClip!=null&&lastClip.isRunning();
   }

   public void stop() {
      if (lastClip!=null&&lastClip.isRunning()) {
         lastClip.stop();
         lastClip=null;
      }
   }

   public void play() {
      if (mute) { return; }
      Clip soundClip=getClip();
      if (soundClip!=null) {
         soundClip.start();
      }
   }

   public void endLoop() {
      if (currentLoopClip!=null) {
         currentLoopClip.loop(0);
         currentLoopClip=null;
      }
   }

   public void loop() {
      if (mute) { return; }
      if (currentLoopClip==null) {
         currentLoopClip=getClip();
         if (currentLoopClip!=null) {
            currentLoopClip.loop(Clip.LOOP_CONTINUOUSLY);
         }
      }
   }

   private Clip getClip() {
      Clip soundClip;
      try {
         if (mixer!=null) {
            soundClip=(Clip) mixer.getLine(dataLineInfo);
         } else {
            soundClip=(Clip) AudioSystem.getLine(dataLineInfo);
         }
         soundClip.open(format, data, 0, data.length);
      } catch (LineUnavailableException e) {
         ok=false;
         return null;
      }
      ok=true;
      lastClip=soundClip;
      return soundClip;
   }
}

