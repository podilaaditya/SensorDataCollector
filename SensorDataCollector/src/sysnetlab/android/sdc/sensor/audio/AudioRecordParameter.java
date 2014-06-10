package sysnetlab.android.sdc.sensor.audio;

public class AudioRecordParameter {
    private int mSamplingRate;
    private AudioChannelIn mChannel;
    private AudioEncoding mEncoding;
    private AudioSource mSource;
    private int mBufferSize;

    AudioRecordParameter(int rate, AudioChannelIn channel, AudioEncoding encoding,
            AudioSource source, int bufferSize) {
        mSamplingRate = rate;
        mChannel = channel;
        mEncoding = encoding;
        mSource = source;
        mBufferSize = bufferSize;
    }

    public AudioRecordParameter() {
    }

    public int getSamplingRate() {
        return mSamplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        mSamplingRate = samplingRate;
    }

    public AudioChannelIn getChannel() {
        return mChannel;
    }

    public void setChannel(AudioChannelIn channel) {
        mChannel = channel;
    }

    public AudioEncoding getEncoding() {
        return mEncoding;
    }

    public void setEncoding(AudioEncoding encoding) {
        mEncoding = encoding;
    }

    public AudioSource getSource() {
        return mSource;
    }

    public void setSource(AudioSource source) {
        mSource = source;
    }

    public int getBufferSize() {
        return mBufferSize;
    }

    public void setMinBufferSize(int minBufferSize) {
        mBufferSize = minBufferSize;
    }  
}