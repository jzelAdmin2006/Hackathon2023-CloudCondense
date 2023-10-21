package tech.bison.trainee.server.business.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RawAudioFormat {
  WAV(".wav"),
  AIFF(".aiff");

  private final String fileEnding;
}
