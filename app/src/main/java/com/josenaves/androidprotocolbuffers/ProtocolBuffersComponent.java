package com.josenaves.androidprotocolbuffers;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={ProtocolBuffersApplication.class})
public interface ProtocolBuffersComponent {
    void inject(MainActivity mainActivity);
}
