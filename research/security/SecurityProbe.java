package research.security;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** One isolated process per property. No external service or exploit gadget. */
public final class SecurityProbe {
    static boolean hookExecuted;
    public static final class Canary implements Serializable {
        private static final long serialVersionUID = 1L;
        private void readObject(ObjectInputStream input) throws Exception {
            hookExecuted = true;
            input.defaultReadObject();
        }
    }
    static Class<?> target;
    static Path store;
    static Object fresh() throws Exception { return target.getConstructor(Path.class).newInstance(store); }
    static Object call(Object object, String method) throws Exception { return target.getMethod(method).invoke(object); }
    static boolean add(Object object, int score, int time, String name) throws Exception {
        return (Boolean) target.getMethod("storeRun", int.class, int.class, String.class).invoke(object, score, time, name);
    }
    static List<?> values(Object object, String method) throws Exception { return (List<?>) call(object,method); }
    static void require(boolean result, String message) { if (!result) throw new AssertionError(message); }
    static void reject(int score, int time, String name) throws Exception {
        Object board = fresh();
        require(!add(board,score,time,name), "Invalid record was accepted by storeRun.");
        require(values(board,"getPlayersNames").isEmpty(), "Invalid record was retained.");
    }
    static void largePersistedRecords() throws Exception {
        Object board=fresh(); require(add(board,1,1,"Probe"),"Valid seed record was rejected."); call(board,"persistAcrossRuns");
        byte[] seed=Files.readAllBytes(store);
        Object loaded=fresh(); require(values(loaded,"getPlayersNames").contains("Probe"),"Seed did not survive reload; persistence precondition failed.");
        int header=0;
        if (seed.length>12 && new DataInputStream(new ByteArrayInputStream(seed)).readInt()==0x41504853) header=12;
        else if (seed.length>4 && new DataInputStream(new ByteArrayInputStream(seed)).readInt()==1) header=4;
        else if (seed.length>1 && (seed[0]&255)==0xac && (seed[1]&255)==0xed) {
            System.out.println("ASTERIA_RESULT\tunknown\tLarge-record adapter does not support Java object serialization. Separate canary check covers dispatch."); return;
        } else if (!new String(seed,StandardCharsets.UTF_8).contains("Probe") || seed.length>4096) {
            System.out.println("ASTERIA_RESULT\tunknown\tUnsupported persistence encoding; no valid-record amplification claim."); return;
        }
        final int records=1000000;
        try (DataOutputStream out=new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(store)))) {
            if(header==12) out.write(seed,0,8);
            if(header>0) out.writeInt(records);
            for(int i=0;i<records;i++) out.write(seed,header,seed.length-header);
        }
        board=fresh();
        require(values(board,"getPlayersNames").size()<=100,"Reload retained more than 100 records.");
    }
    public static void main(String[] args) {
        try {
            target=Class.forName(args[0]); store=Paths.get(args[2]);
            String check=args[1];
            switch(check) {
                case "validRecordRoundTrip": {
                    Object board=fresh(); require(add(board,42,7,"Probe"),"Valid record rejected."); call(board,"persistAcrossRuns");
                    Object loaded=fresh(); require(values(loaded,"getPlayersNames").contains("Probe"),"Valid record did not survive reload."); break;
                }
                case "rejectsNegativeScore": reject(-1,1,"Player"); break;
                case "rejectsNegativeTime": reject(1,-1,"Player"); break;
                case "rejectsNullName": reject(1,1,null); break;
                case "rejectsBlankName": reject(1,1,"   "); break;
                case "rejectsExcessiveName": { char[] chars=new char[1024]; Arrays.fill(chars,'A'); reject(1,1,new String(chars)); break; }
                case "boundsRetainedEntries": {
                    Object board=fresh(); for(int i=0;i<120;i++) require(add(board,i,i,"P"+i),"Valid record rejected.");
                    require(values(board,"getPlayersNames").size()<=100,"More than 100 entries retained."); break;
                }
                case "malformedStoreDoesNotCrash": Files.write(store,"invalid\tNaN\t-1\n\u0000".getBytes(StandardCharsets.UTF_8)); fresh(); break;
                case "oversizedPhysicalLine": {
                    byte[] block=new byte[8192]; Arrays.fill(block,(byte)'A');
                    try(OutputStream out=new BufferedOutputStream(Files.newOutputStream(store))) { for(int i=0;i<8192;i++) out.write(block); }
                    fresh(); break;
                }
                case "nativeDeserializationCanary": {
                    try(ObjectOutputStream out=new ObjectOutputStream(Files.newOutputStream(store))) { out.writeObject(new Canary()); }
                    try { fresh(); } catch(InvocationTargetException rejected) { /* Rejection is allowed, provided no hook executed. */ }
                    require(!hookExecuted,"Test-only deserialization hook executed before record type validation. This is dispatch evidence, not a production RCE demonstration."); break;
                }
                case "largePersistedRecordSet": largePersistedRecords(); return;
                default: throw new IllegalArgumentException("Unknown check: "+check);
            }
            System.out.println("ASTERIA_RESULT\tpass\tDeclared property held for this fixture.");
        } catch(Throwable error) {
            Throwable cause=error; while(cause.getCause()!=null) cause=cause.getCause();
            System.out.println("ASTERIA_RESULT\tfail\t"+cause.getClass().getSimpleName()+": "+String.valueOf(cause.getMessage()).replace('\n',' '));
            System.exit(1);
        }
    }
}
