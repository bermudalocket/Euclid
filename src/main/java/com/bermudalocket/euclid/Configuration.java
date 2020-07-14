package com.bermudalocket.euclid;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    public static final Configuration INSTANCE = new Configuration();

    private final File configFile;

    public int WIREFRAME_RED = 255;
    public int WIREFRAME_GREEN = 0;
    public int WIREFRAME_BLUE = 0;
    public int WIREFRAME_ALPHA = 37;

    public Color getWireframeColor() {
        return new Color(this.WIREFRAME_RED/255f, this.WIREFRAME_GREEN/255f, this.WIREFRAME_BLUE/255f, this.WIREFRAME_ALPHA/100f);
    }

    public final Screen configScreen;

    private Configuration() {
        this.configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "euclid.properties");
        this.load();

        ConfigBuilder builder = ConfigBuilder.create().setTitle(new LiteralText("Euclid Configuration"));
        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

        ConfigCategory wireframeCategory = builder.getOrCreateCategory(new LiteralText("Wireframe"));

        wireframeCategory.addEntry(entryBuilder.startColorField(new LiteralText("Color"), Color.RED.getRGB())
                                               .setSaveConsumer(i -> {

                                               })
                                               .build());
        wireframeCategory.addEntry(entryBuilder
            .startIntSlider(new LiteralText("Red"), 255, 0, 255)
            .setSaveConsumer(i -> {
                this.WIREFRAME_RED = i;
                this.save();
            })
            .build()
        );
        wireframeCategory.addEntry(entryBuilder
            .startIntSlider(new LiteralText("Green"), 0, 0, 255)
            .setSaveConsumer(i -> {
                this.WIREFRAME_GREEN = i;
                this.save();
            })
            .build()
        );
        wireframeCategory.addEntry(entryBuilder
            .startIntSlider(new LiteralText("Blue"), 0, 0, 255)
            .setSaveConsumer(i -> {
                this.WIREFRAME_BLUE = i;
                this.save();
            })
            .build()
        );
        wireframeCategory.addEntry(entryBuilder
            .startIntSlider(new LiteralText("Alpha (%)"), 37, 0, 100)
            .setSaveConsumer(i -> {
                this.WIREFRAME_ALPHA = i;
                this.save();
            })
            .build()
        );

        this.configScreen = builder.build();
    }

    public void load() {
        try {
            if (!configFile.exists() || !configFile.canRead()) {
                this.save();
            }
            FileInputStream in = new FileInputStream(configFile);
            Properties props = new Properties();
            props.load(in);
            in.close();
            WIREFRAME_RED = Integer.parseInt((String) props.computeIfAbsent("wireframe-red", __ -> "255"));
            WIREFRAME_GREEN = Integer.parseInt((String) props.computeIfAbsent("wireframe-green", __ -> "0"));
            WIREFRAME_BLUE = Integer.parseInt((String) props.computeIfAbsent("wireframe-blue", __ -> "0"));
            WIREFRAME_ALPHA = Integer.parseInt((String) props.computeIfAbsent("wireframe-alpha", __ -> "37"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            FileOutputStream out = new FileOutputStream(configFile, false);
            out.write(("wireframe-red=" + WIREFRAME_RED).getBytes());
            out.write("\n".getBytes());
            out.write(("wireframe-green=" + WIREFRAME_GREEN).getBytes());
            out.write("\n".getBytes());
            out.write(("wireframe-blue=" + WIREFRAME_BLUE).getBytes());
            out.write("\n".getBytes());
            out.write(("wireframe-alpha=" + WIREFRAME_ALPHA).getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
