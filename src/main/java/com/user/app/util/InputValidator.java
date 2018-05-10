package com.user.app.util;

import org.apache.commons.lang3.StringUtils;

import com.user.app.model.UserVO;

public class InputValidator {
	public static boolean isValidateInput(UserVO userVo) {
		if (null != userVo && StringUtils.isNotEmpty(userVo.getName()) && StringUtils.isNotEmpty(userVo.getEmail())
				&& StringUtils.isNotEmpty(userVo.getProfession())) {
			AppLogger.getLogger().info("isValidInput:true");
			return true;
		} else {
			AppLogger.getLogger().info("isValidInput:false");
			return false;
		}

	}
}