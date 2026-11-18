package com.infinitegalaxies.planet;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.minecraftforge.common.DimensionManager;

public final class PlanetRegistry {

    public static final int PROVIDER_ID = 1337;
    public static final PlanetTraits DEFAULT = new PlanetTraits("default", 1L, 0);

    private static final Map<Integer, PlanetTraits> BY_DIM = new HashMap<Integer, PlanetTraits>();
    private static final Map<String, PlanetTraits> BY_KEY = new LinkedHashMap<String, PlanetTraits>();

    private PlanetRegistry() {}

    public static PlanetTraits byDim(int dim) {
        return BY_DIM.get(dim);
    }

    public static String key(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    private static File file() {
        File root = DimensionManager.getCurrentSaveRootDirectory();
        return root == null ? null : new File(root, "infinitegalaxies_planets.txt");
    }

    public static synchronized PlanetTraits getOrCreate(String displayName, boolean persist) {
        String k = key(displayName);
        PlanetTraits t = BY_KEY.get(k);
        if (t != null) return t;
        long seed = PlanetTraits.seedOf(k);
        int id = 1000 + (int) ((seed >>> 1) % 100000L);
        while (DimensionManager.isDimensionRegistered(id) || BY_DIM.containsKey(id)) id++;
        t = new PlanetTraits(displayName.trim(), seed, id);
        DimensionManager.registerDimension(id, PROVIDER_ID);
        BY_DIM.put(id, t);
        BY_KEY.put(k, t);
        if (persist) {
            File f = file();
            if (f != null) {
                try {
                    Files.write(
                        f.toPath(),
                        Collections.singletonList(displayName.trim()),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                } catch (Exception e) {
                    com.infinitegalaxies.InfiniteGalaxies.LOG.error("Nao consegui salvar o planeta", e);
                }
            }
        }
        return t;
    }

    public static synchronized void loadAll() {
        clear();
        File f = file();
        if (f == null || !f.exists()) return;
        try {
            List<String> lines = Files.readAllLines(f.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                if (!line.trim().isEmpty()) getOrCreate(line, false);
            }
        } catch (Exception e) {
            com.infinitegalaxies.InfiniteGalaxies.LOG.error("Nao consegui ler os planetas", e);
        }
    }

    public static synchronized void clear() {
        for (Integer id : new ArrayList<Integer>(BY_DIM.keySet())) {
            try {
                DimensionManager.unregisterDimension(id);
            } catch (Exception ignored) {}
        }
        BY_DIM.clear();
        BY_KEY.clear();
    }
}
