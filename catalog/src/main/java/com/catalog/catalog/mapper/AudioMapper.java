package com.catalog.catalog.mapper;

import com.catalog.catalog.model.Audio;
import com.catalog.catalog.model.User;
import com.catalog.catalog.rest.dto.AudioDto;
import com.catalog.catalog.rest.dto.CreateAudioRequest;

public interface AudioMapper {


    AudioDto toAudioDto(Audio audio);

    Audio toAudio(CreateAudioRequest createAudioRequest, User user);
}
