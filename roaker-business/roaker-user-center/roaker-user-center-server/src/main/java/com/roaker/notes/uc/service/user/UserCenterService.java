package com.roaker.notes.uc.service.user;

import com.roaker.notes.uc.dal.dataobject.user.ShareUserInfoDO;

public interface UserCenterService {
    ShareUserInfoDO getByMobile(String mobile);
}
