package com.dlht.ionic.browser;

import android.content.Intent;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Browser")
public class BrowserPlugin extends Plugin {

    @PluginMethod
    public void create(PluginCall call){
        String url = call.getString("url");
        Intent intent = new Intent(getContext(),BrowserActivity.class);
        intent.putExtra("url",url);
        getActivity().startActivity(intent);
        call.resolve();
    }
}
