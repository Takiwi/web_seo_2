package doan.bai_2.dto.user.responses;

import doan.bai_2.dto.BaseResponse;
import doan.bai_2.models.UserEntity;

public class UserResponse extends BaseResponse<UserEntity>{
    private UserEntity user;

    public UserResponse(){}

    public UserResponse(UserEntity user, String message){
        super(message);
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }
}
