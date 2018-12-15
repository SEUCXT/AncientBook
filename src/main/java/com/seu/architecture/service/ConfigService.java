package com.seu.architecture.service;

import com.seu.architecture.model.ViewObject;

import java.io.InputStream;

/**
 * Created by 17858 on 2017-11-23.
 */
public interface ConfigService {

    ViewObject configPermission(InputStream is);

    ViewObject configRole(InputStream is);

}
