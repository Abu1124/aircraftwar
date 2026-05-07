package edu.hitsz.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 音效管理器，使用 Thread 实现音频播放控制。
 * <p>
 * 支持：背景音乐循环播放、Boss专属音乐循环播放、一次性音效触发。
 * </p>
 */
public class AudioManager extends Thread {

    private static final String AUDIO_DIR = "src/vedios/";

    private Clip bgmClip;
    private Clip bossBgmClip;
    private final Map<String, Clip> soundClips = new HashMap<>();

    /** BGM 是否正在播放 */
    private volatile boolean bgmPlaying = false;
    /** Boss BGM 是否正在播放 */
    private volatile boolean bossBgmPlaying = false;

    public AudioManager() {
        loadAudio();
    }

    /**
     * 预加载所有音频文件
     */
    private void loadAudio() {
        try {
            bgmClip = createClip(AUDIO_DIR + "bgm.wav");
            bossBgmClip = createClip(AUDIO_DIR + "bgm_boss.wav");
            soundClips.put("bomb_explosion", createClip(AUDIO_DIR + "bomb_explosion.wav"));
            soundClips.put("bullet_hit", createClip(AUDIO_DIR + "bullet_hit.wav"));
            soundClips.put("game_over", createClip(AUDIO_DIR + "game_over.wav"));
            soundClips.put("get_supply", createClip(AUDIO_DIR + "get_supply.wav"));
        } catch (Exception e) {
            System.err.println("[AudioManager] 音频加载失败: " + e.getMessage());
        }
    }

    /**
     * 创建可循环播放的 Clip
     */
    private Clip createClip(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File file = new File(path);
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        return clip;
    }

    /**
     * 播放背景音乐（循环）
     */
    public synchronized void playBgm() {
        if (bgmClip == null || bgmPlaying) {
            return;
        }
        try {
            bgmClip.stop();
            bgmClip.setFramePosition(0);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmPlaying = true;
        } catch (Exception e) {
            System.err.println("[AudioManager] BGM播放失败: " + e.getMessage());
        }
    }

    /**
     * 停止背景音乐
     */
    public synchronized void stopBgm() {
        if (bgmClip != null && bgmPlaying) {
            bgmClip.stop();
            bgmClip.setFramePosition(0);
            bgmPlaying = false;
        }
    }

    /**
     * 播放 Boss 专属背景音乐（循环），同时停止普通 BGM
     */
    public synchronized void playBossBgm() {
        if (bossBgmClip == null || bossBgmPlaying) {
            return;
        }
        // 先停止普通BGM
        stopBgm();
        try {
            bossBgmClip.stop();
            bossBgmClip.setFramePosition(0);
            bossBgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bossBgmPlaying = true;
        } catch (Exception e) {
            System.err.println("[AudioManager] Boss BGM播放失败: " + e.getMessage());
        }
    }

    /**
     * 停止 Boss 背景音乐
     */
    public synchronized void stopBossBgm() {
        if (bossBgmClip != null && bossBgmPlaying) {
            bossBgmClip.stop();
            bossBgmClip.setFramePosition(0);
            bossBgmPlaying = false;
        }
    }

    /**
     * 播放一次性音效
     *
     * @param type 音效类型：bomb_explosion / bullet_hit / game_over / get_supply
     */
    public synchronized void playSound(String type) {
        Clip clip = soundClips.get(type);
        if (clip == null) {
            return;
        }
        try {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            System.err.println("[AudioManager] 音效播放失败 (" + type + "): " + e.getMessage());
        }
    }

    /**
     * 停止所有音频（游戏结束时调用）
     */
    public synchronized void stopAll() {
        stopBgm();
        stopBossBgm();
    }

    /**
     * 释放所有音频资源
     */
    public synchronized void dispose() {
        stopAll();
        if (bgmClip != null) {
            bgmClip.close();
        }
        if (bossBgmClip != null) {
            bossBgmClip.close();
        }
        for (Clip clip : soundClips.values()) {
            clip.close();
        }
    }

    @Override
    public void run() {
        // 主线程用于音频管理，实际播放由各方法触发
    }
}
