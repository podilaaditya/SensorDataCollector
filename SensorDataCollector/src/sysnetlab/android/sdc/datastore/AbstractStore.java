
package sysnetlab.android.sdc.datastore;

import java.util.List;

import sysnetlab.android.sdc.datacollector.Experiment;

public abstract class AbstractStore {
	
    protected List<Channel> mChannels;

    public abstract class Channel {
        public final static int READ_ONLY = 0x0001;
        public final static int WRITE_ONLY = 0x0002;        
        
        public abstract void open();
        public abstract void write(String s);
        public abstract void write(byte[] buffer, int offset, int length);
        public abstract String read();
        public abstract void reset();
        public abstract void close();

        /**
         * @return the channel description sufficiently to reconstruct a channel
         *         to read the data
         */
        public abstract String describe();
    };

    public abstract void setupNewExperimentStorage(Experiment experiment);
    public abstract void writeExperimentMetaData(Experiment experiment);
    public abstract List<Experiment> listStoredExperiments();
    
    public abstract Channel createChannel(String tag);
    public abstract void closeAllChannels();
}
