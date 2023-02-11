package com.kaliv.myths.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kaliv.myths.dto.authorDtos.CreateAuthorDto;
import com.kaliv.myths.dto.categoryDtos.CreateCategoryDto;
import com.kaliv.myths.dto.museumDtos.CreateMuseumDto;
import com.kaliv.myths.dto.musicDtos.CreateMusicDto;
import com.kaliv.myths.dto.mythCharacterDtos.CreateMythCharacterDto;
import com.kaliv.myths.dto.mythDtos.CreateMythDto;
import com.kaliv.myths.dto.nationalityDtos.CreateNationalityDto;
import com.kaliv.myths.dto.paintingDtos.CreatePaintingDto;
import com.kaliv.myths.dto.poemDtos.CreatePoemDto;
import com.kaliv.myths.dto.statueDtos.CreateStatueDto;
import com.kaliv.myths.dto.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dto.userDtos.UserDto;
import com.kaliv.myths.entity.*;
import com.kaliv.myths.entity.artefacts.*;
import com.kaliv.myths.entity.users.User;
import com.kaliv.myths.mapper.*;

@Configuration
public class MapperConfig {

    @Bean
    public static ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public static AuthorMapper authorMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreateAuthorDto.class, Author.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new AuthorMapper(modelMapper);
    }

    @Bean
    public static CategoryMapper categoryMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreateCategoryDto.class, Category.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new CategoryMapper(modelMapper);
    }

    @Bean
    public static MuseumMapper museumMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreateMuseumDto.class, Museum.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new MuseumMapper(modelMapper);
    }

    @Bean
    public static MusicMapper musicMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreateMusicDto.class, Music.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new MusicMapper(modelMapper);
    }

    @Bean
    public static MythCharacterMapper mythCharacterMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreateMythCharacterDto.class, MythCharacter.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new MythCharacterMapper(modelMapper);
    }

    @Bean
    public static MythMapper mythMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreateMythDto.class, Myth.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new MythMapper(modelMapper);
    }

    @Bean
    public static NationalityMapper nationalityMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreateNationalityDto.class, Nationality.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new NationalityMapper(modelMapper);
    }

    @Bean
    public static PaintingMapper paintingMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreatePaintingDto.class, Painting.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new PaintingMapper(modelMapper);
    }

    @Bean
    public static PoemMapper poemMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreatePoemDto.class, Poem.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new PoemMapper(modelMapper);
    }

    @Bean
    public static StatueMapper statueMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreateStatueDto.class, Statue.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new StatueMapper(modelMapper);
    }

    @Bean
    public static TimePeriodMapper timePeriodMapper(ModelMapper modelMapper) {
        modelMapper.typeMap(CreateTimePeriodDto.class, TimePeriod.class)
                .addMappings(m -> m.skip(BaseEntity::setId))
                .implicitMappings();
        return new TimePeriodMapper(modelMapper);
    }

    @Bean
    public static UserMapper userMapper(ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        modelMapper.typeMap(User.class, UserDto.class)
                .addMappings(m -> m.map(
                        user -> user.getRole().getName(),
                        UserDto::setRole))
                .implicitMappings();
        return new UserMapper(modelMapper, passwordEncoder);
    }
}
