package dapp.dapp.dto;


public class SignedMessageDto {
    private int v;
    private String r;
    private String s;



    public SignedMessageDto(int v, String r, String s) {
        this.v = v;
        this.r = r;
        this.s = s;
    }

    public int getV() {
        return v;
    }

    public SignedMessageDto setV(int v) {
        this.v = v;
        return this;
    }

    public String getR() {
        return r;
    }

    public SignedMessageDto setR(String r) {
        this.r = r;
        return this;
    }

    public String getS() {
        return s;
    }

    public SignedMessageDto setS(String s) {
        this.s = s;
        return this;
    }


}
