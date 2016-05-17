package com.josenaves.androidprotocolbuffers;

import android.app.Application;
import android.util.Log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
public class ProtocolBuffersApplication extends Application {

    private static final String TAG = ProtocolBuffersApplication.class.getSimpleName();

    private ProtocolBuffersComponent protocolBuffersComponent;

    private Realm realm;
    private RealmConfiguration realmConfig;


    @Override
    public void onCreate() {
        super.onCreate();
        protocolBuffersComponent = DaggerProtocolBuffersComponent.builder()
                .protocolBuffersApplication(this)
                .build();


        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(this).build();

        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);
    }

    public Realm getRealm() {
        return realm;
    }

    public ProtocolBuffersComponent getComponent() {
        return protocolBuffersComponent;
    }

    @Provides
    @Singleton
    public Sensors getSensors() {
        Log.d(TAG, "getSensors");
        Sensors sensors = new Sensors(this.getApplicationContext());
        return sensors;
    }
}
