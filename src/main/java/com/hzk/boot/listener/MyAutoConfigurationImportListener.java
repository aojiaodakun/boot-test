package com.hzk.boot.listener;

import org.springframework.boot.autoconfigure.AutoConfigurationImportEvent;
import org.springframework.boot.autoconfigure.AutoConfigurationImportListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class MyAutoConfigurationImportListener implements AutoConfigurationImportListener {


    @Override
    public void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event) {
        List<String> candidateConfigurations = event.getCandidateConfigurations();
        Set<String> exclusions = event.getExclusions();



    }


}

