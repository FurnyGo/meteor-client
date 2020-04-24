package minegame159.meteorclient.settings;

import minegame159.meteorclient.gui.widgets.WIntEdit;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Consumer;

public class IntSetting extends Setting<Integer> {
    private final Integer min, max;
    private final Integer sliderMin, sliderMax;

    private IntSetting(String name, String description, String group, Integer defaultValue, Consumer<Integer> onChanged, Consumer<Setting<Integer>> onModuleActivated, Integer min, Integer max, Integer sliderMin, Integer sliderMax, boolean visible) {
        super(name, description, group, defaultValue, onChanged, onModuleActivated, visible);
        this.min = min;
        this.max = max;
        this.sliderMin = sliderMin;
        this.sliderMax = sliderMax;

        widget = new WIntEdit(sliderMin != null ? sliderMin : 0, sliderMax != null ? sliderMax : 10, get());
        ((WIntEdit) widget).action = intEdit -> {
            if (!set(intEdit.get())) intEdit.set(get());
        };
    }

    @Override
    protected Integer parseImpl(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    protected void resetWidget() {
        ((WIntEdit) widget).set(get());
    }

    @Override
    protected boolean isValueValid(Integer value) {
        return (min == null || value >= min) && (max == null || value <= max);
    }

    @Override
    protected String generateUsage() {
        String usage = "#blue";

        if (min == null) usage += "inf";
        else usage += min;

        usage += " #gray- #blue";

        if (max == null) usage += "inf";
        else usage += max;

        return usage;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = saveGeneral();
        tag.putInt("value", get());
        return tag;
    }

    @Override
    public Integer fromTag(CompoundTag tag) {
        set(tag.getInt("value"));

        return get();
    }

    public static class Builder {
        private String name = "undefined", description = "";
        private String group;
        private Integer defaultValue;
        private Consumer<Integer> onChanged;
        private Consumer<Setting<Integer>> onModuleActivated;
        private Integer min, max;
        private Integer sliderMin, sliderMax;
        private boolean visible = true;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder group(String group) {
            this.group = group;
            return this;
        }

        public Builder defaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder onChanged(Consumer<Integer> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public Builder onModuleActivated(Consumer<Setting<Integer>> onModuleActivated) {
            this.onModuleActivated = onModuleActivated;
            return this;
        }

        public Builder min(int min) {
            this.min = min;
            return this;
        }

        public Builder max(int max) {
            this.max = max;
            return this;
        }

        public Builder sliderMin(int min) {
            sliderMin = min;
            return this;
        }

        public Builder sliderMax(int max) {
            sliderMax = max;
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public IntSetting build() {
            return new IntSetting(name, description, group, defaultValue, onChanged, onModuleActivated, min, max, sliderMin, sliderMax, visible);
        }
    }
}
