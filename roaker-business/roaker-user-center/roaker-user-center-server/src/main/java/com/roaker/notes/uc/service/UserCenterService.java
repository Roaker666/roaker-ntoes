package com.roaker.notes.uc.service;

import com.roaker.notes.uc.dal.dataobject.ShareUserInfoDO;

public interface UserCenterService {
    ShareUserInfoDO getByMobile(String mobile);
}
