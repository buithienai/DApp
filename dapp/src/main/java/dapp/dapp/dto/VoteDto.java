package dapp.dapp.dto;

public class VoteDto {

    private int roleIndex;

    private int profileId;

    private SignatureDto signatureDto;

    public int getRoleIndex() {
        return roleIndex;
    }

    public VoteDto setRoleIndex(int roleIndex) {
        this.roleIndex = roleIndex;
        return this;
    }

    public SignatureDto getSignatureDto() {
        return signatureDto;
    }

    public VoteDto setSignatureDto(SignatureDto signatureDto) {
        this.signatureDto = signatureDto;
        return this;
    }

    public int getProfileId() {
        return profileId;
    }

    public VoteDto setProfileId(int profileId) {
        this.profileId = profileId;
        return this;
    }
}
