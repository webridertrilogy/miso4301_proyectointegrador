package co.uniandes.sisinfo.serviciosfuncionales.cache;

import java.util.HashMap;

/**
 * @author German Florez
 * Uniandes
 */
public class SisinfoCache<V> {

    private HashMap<Object, entrada> mapa;
    /**
     * milisegundos de vida de una consulta
     */
    private long timeToLive;
    private boolean eternal;

    public SisinfoCache(long timeToLive, boolean eternal) {
        mapa=new HashMap<Object, entrada>();
        this.timeToLive = timeToLive;
        this.eternal = eternal;
    }

    public SisinfoCache() {
        this(30000, false);
    }

    public boolean isEternal() {
        return eternal;
    }

    public void setEternal(boolean eternal) {
        this.eternal = eternal;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    

    public void put(Object key, V val) {
        mapa.put(key, new entrada(val, System.currentTimeMillis()));
    }

    public V get(Object key) {
        entrada en = mapa.get(key);
        if(en==null){
            return null;
        }
        V val = en.ref;
        if (eternal || Math.abs(System.currentTimeMillis()-en.consulta)<timeToLive) {
            return val;
        }
        return null;
    }

    public void clear(){
         mapa.clear();
    }

    private class entrada {

        V ref;
        long consulta;

        public entrada(V ref, long consulta) {
            this.ref = ref;
            this.consulta = consulta;
        }
    }
}
