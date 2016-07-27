package com.makingdevs.mybarista.common

import groovy.transform.CompileStatic

@CompileStatic
interface LoadDataView {

    void enableLoadingData(Boolean enable)

    void showError(String errorMessage)
}