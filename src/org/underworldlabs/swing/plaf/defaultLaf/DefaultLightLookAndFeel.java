package org.underworldlabs.swing.plaf.defaultLaf;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class DefaultLightLookAndFeel extends FlatLightLaf {

    @Override
    public UIDefaults getDefaults() {
        return DefaultLookAndFeel.baseDefaults(super.getDefaults());
    }

    @Override
    public void initialize() {
        super.initialize();
        DefaultLookAndFeel.baseInitialize();
    }

}
