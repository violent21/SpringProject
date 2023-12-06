package com.springproject.model;

import com.springproject.repos.*;
import org.springframework.stereotype.Component;

@Component
public class RepositoryContainer {
    private final FixtureRepository fixtureRepository;
    private final TeamsStatisticsRepository teamStatisticsRepository;
    private final PlayersSquadsRepository playersSquadsRepository;
    private final VenuesInfoRepository venuesInfoRepository;
    private final EventsRepository eventsRepository;
    private final HeadToHeadRepository headToHeadRepository;
    private final MainLineupsRepository mainLineupsRepository;
    private final StartXILineupsRepository startXILineupsRepository;
    private final SubstitutesLineupsRepository substitutesLineupsRepository;

    public RepositoryContainer(
            FixtureRepository fixtureRepository,
            TeamsStatisticsRepository teamStatisticsRepository,
            PlayersSquadsRepository playersSquadsRepository,
            VenuesInfoRepository venuesInfoRepository,
            EventsRepository eventsRepository,
            HeadToHeadRepository headToHeadRepository,
            MainLineupsRepository mainLineupsRepository,
            StartXILineupsRepository startXILineupsRepository,
            SubstitutesLineupsRepository substitutesLineupsRepository) {
        this.fixtureRepository = fixtureRepository;
        this.teamStatisticsRepository = teamStatisticsRepository;
        this.playersSquadsRepository = playersSquadsRepository;
        this.venuesInfoRepository = venuesInfoRepository;
        this.eventsRepository = eventsRepository;
        this.headToHeadRepository = headToHeadRepository;
        this.mainLineupsRepository = mainLineupsRepository;
        this.startXILineupsRepository = startXILineupsRepository;
        this.substitutesLineupsRepository = substitutesLineupsRepository;
    }

    public FixtureRepository getFixtureRepository() {
        return fixtureRepository;
    }

    public TeamsStatisticsRepository getTeamStatisticsRepository() {
        return teamStatisticsRepository;
    }

    public PlayersSquadsRepository getPlayersSquadsRepository() {
        return playersSquadsRepository;
    }

    public VenuesInfoRepository getVenuesInfoRepository() {
        return venuesInfoRepository;
    }

    public EventsRepository getEventsRepository() {
        return eventsRepository;
    }

    public HeadToHeadRepository getHeadToHeadRepository() {
        return headToHeadRepository;
    }

    public MainLineupsRepository getMainLineupsRepository() {
        return mainLineupsRepository;
    }

    public StartXILineupsRepository getStartXILineupsRepository() {
        return startXILineupsRepository;
    }

    public SubstitutesLineupsRepository getSubstitutesLineupsRepository() {
        return substitutesLineupsRepository;
    }
}
