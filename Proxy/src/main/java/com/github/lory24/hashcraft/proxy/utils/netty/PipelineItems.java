package com.github.lory24.hashcraft.proxy.utils.netty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PipelineItems {
    LEGACY_DECODER("legacy-decoder"),
    FRAME_DECODER("frame-decoder"),
    MINECRAFT_DECODER("minecraft-decoder"),
    MINECRAFT_ENCODER("minecraft-encoder"),
    FRAME_ENCODER("frame-encoder"),
    BOSS_HANDLER("handler"),
    ;

    /**
     * The name used in the netty pipeline
     */
    @Getter
    private final String name;


    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum class should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return this.name;
    }
}
