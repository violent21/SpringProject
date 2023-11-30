package com.springproject;

import com.springproject.model.*;
import com.springproject.repos.*;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class FootballController {
    @Autowired
    private FixtureRepository fixtureRepository;
    @Autowired
    private TeamsStatisticsRepository teamStatisticsRepository;
    @Autowired
    private PlayersSquadsRepository playersSquadsRepository;
    @Autowired
    private VenuesInfoRepository venuesInfoRepository;
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private HeadToHeadRepository headToHeadRepository;
    @Autowired
    private MainLineupsRepository mainLineupsRepository;
    @Autowired
    private StartXILineupsRepository startXILineupsRepository;
    @Autowired
    private SubstitutesLineupsRepository substitutesLineupsRepository;
    private LocalDateTime lastUpdateTimestamp;
    private static final Duration REFRESH_INTERVAL = Duration.ofHours(1);
    private Map<String, Object> result = new HashMap<>();
    private Map<String, Object> result1 = new HashMap<>();
    String league = "135";
    String season = "2023";
    String fromDate = "2023-10-30";
    String toDate = "2023-11-03";
    String last = "5";
    String apiKey = "739fbef17c8cda1256722a1d0ae58ba3";

    @Cacheable("apiData")
    public Map<String, Object> fetchDataFromApi(String league, String season, String fromDate, String toDate) {

        String endpoint = "fixtures?league=" + league + "&season=" + season + "&from=" + fromDate + "&to=" + toDate;
        String apiUrl = "https://v3.football.api-sports.io/" + endpoint;

        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                .header("Accept", "application/json")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "v3.football.api-sports.io")
                .asJson();

        return response.getBody().getObject().toMap();
    }

    @Cacheable("apiDataStatistics")
    public Map<String, Object> fetchFixtureStatistics(long fixtureId) {
        String endpoint = "fixtures/statistics?fixture=" + fixtureId;
        String apiUrl = "https://v3.football.api-sports.io/" + endpoint;

        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                .header("Accept", "application/json")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "v3.football.api-sports.io")
                .asJson();

        return response.getBody().getObject().toMap();
    }

    @Cacheable("playersSquads")
    public Map<String, Object> fetchPlayersSquads(long teamId) {
        String endpoint = "players/squads?team=" + teamId;
        String apiUrl = "https://v3.football.api-sports.io/" + endpoint;

        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                .header("Accept", "application/json")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "v3.football.api-sports.io")
                .asJson();

        return response.getBody().getObject().toMap();
    }

    @Cacheable("venuesInfo")
    public Map<String, Object> fetchVenuesInfo(long venueId) {
        String endpoint = "/venues?id=" + venueId;
        String apiUrl = "https://v3.football.api-sports.io/" + endpoint;

        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                .header("Accept", "application/json")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "v3.football.api-sports.io")
                .asJson();

        return response.getBody().getObject().toMap();
    }

    @Cacheable("events")
    public Map<String, Object> fetchEvents(long fixtureId) {
        String endpoint = "/fixtures/events?fixture=" + fixtureId;
        String apiUrl = "https://v3.football.api-sports.io/" + endpoint;

        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                .header("Accept", "application/json")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "v3.football.api-sports.io")
                .asJson();

        return response.getBody().getObject().toMap();
    }

    @Cacheable("headToHead")
    public Map<String, Object> fetchHeadToHead(long homeTeamId, long awayTeamId) {
        String endpoint = "/fixtures/headtohead?h2h=" + homeTeamId + "-" + awayTeamId + "&last=" + last;
        String apiUrl = "https://v3.football.api-sports.io/" + endpoint;

        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                .header("Accept", "application/json")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "v3.football.api-sports.io")
                .asJson();

        return response.getBody().getObject().toMap();
    }

    public Map<String, Object> fetchLineups(long fixtureId) {
        String endpoint = "/fixtures/lineups?fixture=" + fixtureId;
        String apiUrl = "https://v3.football.api-sports.io/" + endpoint;

        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                .header("Accept", "application/json")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "v3.football.api-sports.io")
                .asJson();

        return response.getBody().getObject().toMap();
    }

    public void processFixtureStatistics() {
        teamStatisticsRepository.deleteAll();
        List<Map<String, Object>> fixtures = (List<Map<String, Object>>) result.get("response");
        if (fixtures != null) {
            for (Map<String, Object> fixtureData : fixtures) {
                Map<String, Object> fixtureInfo = (Map<String, Object>) fixtureData.get("fixture");

                long fixtureId = Long.parseLong(String.valueOf(fixtureInfo.get("id")));
                result1 = fetchFixtureStatistics(fixtureId);
                saveStatisticsToDatabase(fixtureId);
            }
        }
    }

    public void processLineups() {
        mainLineupsRepository.deleteAll();
        startXILineupsRepository.deleteAll();
        substitutesLineupsRepository.deleteAll();
        List<Map<String, Object>> fixtures = (List<Map<String, Object>>) result.get("response");
        if (fixtures != null) {
            for (Map<String, Object> fixtureData : fixtures) {
                Map<String, Object> fixtureInfo = (Map<String, Object>) fixtureData.get("fixture");

                long fixtureId = Long.parseLong(String.valueOf(fixtureInfo.get("id")));
                result1 = fetchLineups(fixtureId);
                saveLineupsToDatabase(fixtureId);
            }
        }
    }

    private void saveLineupsToDatabase(long fixtureId) {
        List<Map<String, Object>> lineupsMap = (List<Map<String, Object>>) result1.get("response");
        if (lineupsMap != null) {
            for (Map<String, Object> lineupsData : lineupsMap) {
                Map<String, Object> teamData = (Map<String, Object>) lineupsData.get("team");
                Map<String, Object> coachData = (Map<String, Object>) lineupsData.get("coach");
                Map<String, Object> colorsPlayerData = (Map<String, Object>) ((Map<String, Object>) teamData.get("colors")).get("player");
                Map<String, Object> colorsGoalkeeperData = (Map<String, Object>) ((Map<String, Object>) teamData.get("colors")).get("goalkeeper");
                List<Map<String, Object>> startXIData = (List<Map<String, Object>>) lineupsData.get("startXI");
                List<Map<String, Object>> substitutesData = (List<Map<String, Object>>) lineupsData.get("substitutes");

                MainLineups mainLineups = new MainLineups();
                mainLineups.setFixtureId(fixtureId);
                mainLineups.setTeamId(Long.valueOf(teamData.get("id").toString()));
                mainLineups.setTeamName(String.valueOf(teamData.get("name")));
                mainLineups.setPlayerPrimaryColor(String.valueOf(colorsPlayerData.get("primary")));
                mainLineups.setPlayerNumberColor(String.valueOf(colorsPlayerData.get("number")));
                mainLineups.setPlayerBorderColor(String.valueOf(colorsPlayerData.get("border")));
                mainLineups.setGoalkeeperPrimaryColor(String.valueOf(colorsGoalkeeperData.get("primary")));
                mainLineups.setGoalkeeperNumberColor(String.valueOf(colorsGoalkeeperData.get("number")));
                mainLineups.setGoalkeeperBorderColor(String.valueOf(colorsGoalkeeperData.get("border")));
                mainLineups.setCoachId(Long.valueOf(coachData.get("id").toString()));
                mainLineups.setCoachName(String.valueOf(coachData.get("name")));
                mainLineups.setCoachPhoto(String.valueOf(coachData.get("photo")));
                mainLineups.setFormation(String.valueOf(lineupsData.get("formation")));
                mainLineupsRepository.save(mainLineups);

                for (Map<String, Object> playerData : startXIData) {
                    Map<String, Object> player = (Map<String, Object>) playerData.get("player");

                    StartXILineups startXILineups = new StartXILineups();
                    startXILineups.setFixtureId(fixtureId);
                    startXILineups.setTeamId(Long.valueOf(teamData.get("id").toString()));
                    startXILineups.setPlayerId(Long.valueOf(player.get("id").toString()));
                    startXILineups.setPlayerName(String.valueOf(player.get("name")));
                    startXILineups.setPlayerNumber(String.valueOf(player.get("number").toString()));
                    startXILineups.setPlayerPos(String.valueOf(player.get("pos")));
                    startXILineups.setPlayerGrid(String.valueOf(player.get("grid")));
                    startXILineupsRepository.save(startXILineups);
                }

                for (Map<String, Object> substituteData : substitutesData) {
                    Map<String, Object> substitute = (Map<String, Object>) substituteData.get("player");
                    SubstitutesLineups substitutesLineups = new SubstitutesLineups();
                    substitutesLineups.setFixtureId(fixtureId);
                    substitutesLineups.setTeamId(Long.valueOf(teamData.get("id").toString()));
                    substitutesLineups.setPlayerId(Long.valueOf(substitute.get("id").toString()));
                    substitutesLineups.setPlayerName(String.valueOf(substitute.get("name")));
                    substitutesLineups.setPlayerNumber(String.valueOf(substitute.get("number").toString()));
                    substitutesLineups.setPlayerPos(String.valueOf(substitute.get("pos")));
                    substitutesLineups.setPlayerGrid(String.valueOf(substitute.get("grid")));
                    substitutesLineupsRepository.save(substitutesLineups);
                }
            }
        }
    }

    public void processPlayersSquads() {
        playersSquadsRepository.deleteAll();
        List<Map<String, Object>> fixtures = (List<Map<String, Object>>) result.get("response");
        if (fixtures != null) {
            for (Map<String, Object> fixtureData : fixtures) {
                Map<String, Object> homeTeam = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("teams")).get("home");
                Map<String, Object> awayTeam = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("teams")).get("away");

                long homeTeamId = Long.parseLong(String.valueOf(homeTeam.get("id")));
                if (playersSquadsRepository.findByTeamId(homeTeamId).isEmpty()) {
                    result1 = fetchPlayersSquads(homeTeamId);
                    savePlayersSquadsToDatabase();
                }

                long awayTeamId = Long.parseLong(String.valueOf(awayTeam.get("id")));
                if (playersSquadsRepository.findByTeamId(awayTeamId).isEmpty()) {
                    result1 = fetchPlayersSquads(awayTeamId);
                    savePlayersSquadsToDatabase();
                }
            }
        }
    }

    public void processVenuesInfo() {
        List<Map<String, Object>> fixtures = (List<Map<String, Object>>) result.get("response");
        if (fixtures != null) {
            for (Map<String, Object> fixtureData : fixtures) {
                Map<String, Object> venue = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("fixture")).get("venue");

                long venueId = Long.parseLong(String.valueOf(venue.get("id")));
                result1 = fetchVenuesInfo(venueId);
                saveVenuesInfoToDatabase();
            }
        }
    }

    public void processEvents() {
        eventsRepository.deleteAll();
        List<Map<String, Object>> fixtures = (List<Map<String, Object>>) result.get("response");
        if (fixtures != null) {
            for (Map<String, Object> fixtureData : fixtures) {
                Map<String, Object> fixtureInfo = (Map<String, Object>) fixtureData.get("fixture");

                long fixtureId = Long.parseLong(String.valueOf(fixtureInfo.get("id")));
                result1 = fetchEvents(fixtureId);
                saveEventsToDatabase(fixtureId);
            }
        }
    }

    public void processHeadToHead() {
        headToHeadRepository.deleteAll();
        List<Map<String, Object>> fixtures = (List<Map<String, Object>>) result.get("response");
        if (fixtures != null) {
            for (Map<String, Object> fixtureData : fixtures) {
                Map<String, Object> homeTeam = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("teams")).get("home");
                Map<String, Object> awayTeam = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("teams")).get("away");

                long homeTeamId = Long.parseLong(String.valueOf(homeTeam.get("id")));
                long awayTeamId = Long.parseLong(String.valueOf(awayTeam.get("id")));
                String homePlusAway = homeTeamId + "-" + awayTeamId;
                result1 = fetchHeadToHead(homeTeamId, awayTeamId);
                saveHeadToHeadToDatabase(homePlusAway);
            }
        }
    }

    private void saveHeadToHeadToDatabase(String homePlusAway) {
        List<Map<String, Object>> headToHeadMap = (List<Map<String, Object>>) result1.get("response");
        if (headToHeadMap != null) {
            for (Map<String, Object> headToHeadData : headToHeadMap) {
                Map<String, Object> fixtureData = (Map<String, Object>) headToHeadData.get("fixture");
                Map<String, Object> venue = (Map<String, Object>) ((Map<String, Object>) headToHeadData.get("fixture")).get("venue");
                Map<String, Object> homeTeamData = (Map<String, Object>) ((Map<String, Object>) headToHeadData.get("teams")).get("home");
                Map<String, Object> awayTeamData = (Map<String, Object>) ((Map<String, Object>) headToHeadData.get("teams")).get("away");
                Map<String, Object> goalsData = (Map<String, Object>) headToHeadData.get("goals");

                HeadToHead headToHead = new HeadToHead();
                headToHead.setId(Long.valueOf(fixtureData.get("id").toString()));
                headToHead.setTeamsId(homePlusAway);
                headToHead.setDate(String.valueOf(fixtureData.get("date")));
                headToHead.setVenueName(String.valueOf(venue.get("name")));
                headToHead.setVenueCity(String.valueOf(venue.get("city")));
                headToHead.setHomeTeamName(String.valueOf(homeTeamData.get("name")));
                headToHead.setHomeTeamLogo(String.valueOf(homeTeamData.get("logo")));
                headToHead.setHomeGoals(Integer.parseInt(goalsData.get("home").toString()));
                headToHead.setAwayTeamName(String.valueOf(awayTeamData.get("name")));
                headToHead.setAwayTeamLogo(String.valueOf(awayTeamData.get("logo")));
                headToHead.setAwayGoals(Integer.parseInt(goalsData.get("away").toString()));

                headToHeadRepository.save(headToHead);
            }
        }
    }

    @Scheduled(fixedRate = 3600000)
    public void updateData() {
        Map<String, Object> newData = fetchDataFromApi(league, season, fromDate, toDate);

        if (!newData.equals(result)) {
            saveDataToDatabase(newData);
            lastUpdateTimestamp = LocalDateTime.now();
            result = newData;
        }
    }

    private void saveEventsToDatabase(long fixtureId) {
        List<Map<String, Object>> eventsMap = (List<Map<String, Object>>) result1.get("response");
        if (eventsMap != null) {
            for (Map<String, Object> eventsData : eventsMap) {
                Map<String, Object> time = (Map<String, Object>) eventsData.get("time");
                Map<String, Object> team = (Map<String, Object>) eventsData.get("team");
                Map<String, Object> player = (Map<String, Object>) eventsData.get("player");
                Map<String, Object> assist = (Map<String, Object>) eventsData.get("assist");

                Events events = new Events();
                events.setFixtureId(fixtureId);
                events.setElapsedTime(Long.valueOf(time.get("elapsed").toString()));
                events.setExtraTime(String.valueOf(time.get("extra")));
                events.setTeamId(String.valueOf(team.get("id")));
                events.setTeamName(String.valueOf(team.get("name")));
                events.setTeamLogo(String.valueOf(team.get("logo")));
                events.setPlayerId(String.valueOf(player.get("id")));
                events.setPlayerName(String.valueOf(player.get("name")));
                events.setAssistId(assist != null ? String.valueOf(assist.get("id")) : null);
                events.setAssistName(assist != null ? (String) assist.get("name") : null);
                events.setType(String.valueOf(eventsData.get("type")));
                events.setDetail(String.valueOf(eventsData.get("detail")));
                events.setComments(String.valueOf(eventsData.get("comments")));

                eventsRepository.save(events);
            }
        }
    }

    private void saveVenuesInfoToDatabase() {
        List<Map<String, Object>> venues = (List<Map<String, Object>>) result1.get("response");
        if (venues != null) {
            for (Map<String, Object> venueData : venues) {
                VenuesInfo venue = new VenuesInfo();
                venue.setId(Long.parseLong(String.valueOf(venueData.get("id"))));
                venue.setName(String.valueOf(venueData.get("name")));
                venue.setAddress(String.valueOf(venueData.get("address")));
                venue.setCity(String.valueOf(venueData.get("city")));
                venue.setCountry(String.valueOf(venueData.get("country")));
                venue.setCapacity(String.valueOf(venueData.get("capacity")));
                venue.setSurface(String.valueOf(venueData.get("surface")));
                venue.setImage(String.valueOf(venueData.get("image")));

                venuesInfoRepository.save(venue);
            }
        }
    }

    private void savePlayersSquadsToDatabase() {
        List<Map<String, Object>> squads = (List<Map<String, Object>>) result1.get("response");
        if (squads != null) {
            for (Map<String, Object> squadsData : squads) {
                Map<String, Object> teamData = (Map<String, Object>) squadsData.get("team");

                Long teamId = Long.valueOf(String.valueOf(teamData.get("id")));
                String teamName = String.valueOf(teamData.get("name"));
                String teamLogo = String.valueOf(teamData.get("logo"));

                List<Map<String, Object>> players = (List<Map<String, Object>>) squadsData.get("players");
                if (players != null && !players.isEmpty()) {
                    for (Map<String, Object> playerData : players) {
                        PlayersSquads player = new PlayersSquads();

                        player.setTeamId(teamId);
                        player.setTeamName(teamName);
                        player.setTeamLogo(teamLogo);

                        player.setPlayerId(String.valueOf(playerData.get("id")));
                        player.setPlayerName(String.valueOf(playerData.get("name")));
                        player.setPlayerAge(String.valueOf(playerData.get("age")));
                        player.setPlayerNumber(String.valueOf(playerData.get("number")));
                        player.setPlayerPosition(String.valueOf(playerData.get("position")));
                        player.setPlayerPhoto(String.valueOf(playerData.get("photo")));

                        playersSquadsRepository.save(player);
                    }
                }
            }
        }
    }

    private void saveStatisticsToDatabase(long fixtureId) {
        List<Map<String, Object>> statistics = (List<Map<String, Object>>) result1.get("response");
        if (statistics != null) {
            for (Map<String, Object> statisticsData : statistics) {
                Map<String, Object> team = (Map<String, Object>) statisticsData.get("team");
                List<Map<String, Object>> statisticsList = (List<Map<String, Object>>) statisticsData.get("statistics");

                TeamsStatistics teamStatistics = new TeamsStatistics();
                teamStatistics.setFixtureId(fixtureId);
                teamStatistics.setTeamId(Long.valueOf(String.valueOf(team.get("id"))));
                teamStatistics.setTeamName(String.valueOf(team.get("name")));
                for (Map<String, Object> statData : statisticsList) {
                    String type = (String) statData.get("type");
                    String value = String.valueOf(statData.get("value"));

                    switch (type) {
                        case "Shots on Goal":
                            teamStatistics.setShotsOnGoal(value);
                            break;
                        case "Shots off Goal":
                            teamStatistics.setShotsOffGoal(value);
                            break;
                        case "Total Shots":
                            teamStatistics.setTotalShots(value);
                            break;
                        case "Blocked Shots":
                            teamStatistics.setBlockedShots(value);
                            break;
                        case "Shots insidebox":
                            teamStatistics.setShotsInsidebox(value);
                            break;
                        case "Shots outsidebox":
                            teamStatistics.setShotsOutsidebox(value);
                            break;
                        case "Fouls":
                            teamStatistics.setFouls(value);
                            break;
                        case "Corner Kicks":
                            teamStatistics.setCornerKicks(value);
                            break;
                        case "Offsides":
                            teamStatistics.setOffsides(value);
                            break;
                        case "Ball Possession":
                            teamStatistics.setBallPossession(value);
                            break;
                        case "Yellow Cards":
                            teamStatistics.setYellowCards(value);
                            break;
                        case "Red Cards":
                            teamStatistics.setRedCards(value);
                            break;
                        case "Goalkeeper Saves":
                            teamStatistics.setGoalkeeperSaves(value);
                            break;
                        case "Total passes":
                            teamStatistics.setTotalPasses(value);
                            break;
                        case "Passes accurate":
                            teamStatistics.setPassesAccurate(value);
                            break;
                        case "Passes %":
                            teamStatistics.setPassesPrcnt(value);
                            break;
                    }
                }
                teamStatisticsRepository.save(teamStatistics);
            }
        }
    }

    private void saveDataToDatabase(Map<String, Object> data) {
        fixtureRepository.deleteAll();

        List<Map<String, Object>> fixtures = (List<Map<String, Object>>) result.get("response");
        if (fixtures != null) {
            for (Map<String, Object> fixtureData : fixtures) {
                Map<String, Object> fixtureInfo = (Map<String, Object>) fixtureData.get("fixture");
                Map<String, Object> homeTeam = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("teams")).get("home");
                Map<String, Object> awayTeam = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("teams")).get("away");

                Fixture fixture = new Fixture();
                fixture.setId(Long.valueOf(String.valueOf(fixtureInfo.get("id"))));
                fixture.setDate(String.valueOf(fixtureInfo.get("date")));
                fixture.setHomeTeamName(String.valueOf(homeTeam.get("name")));
                fixture.setHomeTeamId(String.valueOf(homeTeam.get("id")));
                fixture.setAwayTeamName(String.valueOf(awayTeam.get("name")));
                fixture.setAwayTeamId(String.valueOf(awayTeam.get("id")));

                fixture.setReferee(String.valueOf(fixtureInfo.get("referee")));
                Map<String, Object> fixtureStatus = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("fixture")).get("status");
                if (fixtureStatus.get("long") != null) {
                    fixture.setStatus(String.valueOf(fixtureStatus.get("long")));
                }
                Map<String, Object> venue = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("fixture")).get("venue");
                fixture.setVenueId(String.valueOf(venue.get("id")));
                if (venue.get("name") != null) {
                    fixture.setVenueName(String.valueOf(venue.get("name")));
                }
                if (venue.get("city") != null) {
                    fixture.setVenueCity(String.valueOf(venue.get("city")));
                }
                Map<String, Object> league = (Map<String, Object>) fixtureData.get("league");
                fixture.setLeagueName(String.valueOf(league.get("name")));
                fixture.setLeagueCountry(String.valueOf(league.get("country")));
                fixture.setLeagueLogo(String.valueOf(league.get("logo")));
                fixture.setLeagueFlag(String.valueOf(league.get("flag")));
                fixture.setHomeTeamLogo(String.valueOf(homeTeam.get("logo")));
                if (homeTeam.get("winner") != null) {
                    fixture.setHomeTeamWinner(String.valueOf(homeTeam.get("winner").toString()));
                }
                fixture.setAwayTeamLogo(String.valueOf(awayTeam.get("logo")));
                if (awayTeam.get("winner") != null) {
                    fixture.setAwayTeamWinner(String.valueOf(awayTeam.get("winner").toString()));
                }
                Map<String, Object> goals = (Map<String, Object>) fixtureData.get("goals");
                if (goals != null) {
                    if (goals.get("home") != null) {
                        fixture.setHomeGoals(Integer.parseInt(goals.get("home").toString()));
                    }
                    if (goals.get("away") != null) {
                        fixture.setAwayGoals(Integer.parseInt(goals.get("away").toString()));
                    }
                }
                fixtureRepository.save(fixture);
            }
        }
    }

    private boolean isDataStale() {
        if (lastUpdateTimestamp == null) {
            return true;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        Duration timeElapsed = Duration.between(lastUpdateTimestamp, currentTime);

        return timeElapsed.compareTo(REFRESH_INTERVAL) > 0;
    }

//    @GetMapping("/leagues")
//    @ResponseBody
//    public Map<String, Object> leagues() {
//        result = fetchDataFromApi(league, season, fromDate, toDate);
//
//        return result;
//    }

    @GetMapping("/matches")
    public String showMatches(Model model) {
//        if (isDataStale()) {
//            result = fetchDataFromApi(league, season, fromDate, toDate);
//            if (result != null) {
//                saveDataToDatabase(result);
//                processFixtureStatistics();
//                processPlayersSquads();
//                processVenuesInfo();
//                processEvents();
//                processHeadToHead();
//                processLineups();
//                lastUpdateTimestamp = LocalDateTime.now();
//            }
//        }

        List<Fixture> matches = fixtureRepository.findAll();

        if (!matches.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            for (Fixture match : matches) {
                String formattedDate = formatter.format(OffsetDateTime.parse(match.getDate()));
                match.setFormattedDate(formattedDate);
            }

            model.addAttribute("matches", matches);
            return "matches";
        } else {
            return "match_not_found";
        }
        //        List<Fixture> matches = fixtureRepository.findAll();
//        model.addAttribute("matches", matches);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//
//        for (Fixture match : matches) {
//            if (match.getDate() != null) {
//                OffsetDateTime offsetDateTime = OffsetDateTime.parse(match.getDate());
//                match.setParsedDate(offsetDateTime);
//            }
//        }
//        return "matches";
    }

    @GetMapping("/match/{id}")
    public String showMatchDetails(@PathVariable("id") Long id, Model model) {
        List<TeamsStatistics> matchStatistics = teamStatisticsRepository.findByFixtureId(id);
        //System.out.println("matchStatistics size: " + matchStatistics.size());
        //matchStatistics.forEach(stat -> System.out.println("TeamName: " + stat.getTeamName() + ", ShotsOnGoal: " + stat.getShotsOnGoal() + ", YellowCard: " + stat.getYellowCards()+ ", TotalShots: " + stat.getTotalShots()));
        if (!matchStatistics.isEmpty()) {
            model.addAttribute("matchStatistics", matchStatistics);
            return "match_details";
        } else {
            return "match_not_found";
        }
    }

    @GetMapping("/team/{id}")
    public String showSquads(@PathVariable("id") Long id, Model model) {
        List<PlayersSquads> squads = playersSquadsRepository.findByTeamId(id);
        if (!squads.isEmpty()) {
            model.addAttribute("squads", squads);
            return "players_squads";
        } else {
            return "match_not_found";
        }
    }

    @GetMapping("/venue/{id}")
    public String showVenue(@PathVariable("id") Long id, Model model) {
        Optional<VenuesInfo> venue = venuesInfoRepository.findById(id);
        if (venue.isPresent()) {
            model.addAttribute("venue", venue);
            return "venue_info";
        } else {
            return "match_not_found";
        }
    }

    @GetMapping("/events/{id}")
    public String showEvents(@PathVariable("id") Long id, Model model) {
        List<Events> events = eventsRepository.findByFixtureId(id);
        if (!events.isEmpty()) {
            model.addAttribute("events", events);
            return "events";
        } else {
            return "match_not_found";
        }
    }

    @GetMapping("/headToHead/{id}")
    public String showHeadToHead(@PathVariable("id") String id, Model model) {
        List<HeadToHead> headToHeadList = headToHeadRepository.findByTeamsId(id);

        if (!headToHeadList.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            for (HeadToHead headToHead : headToHeadList) {
                String formattedDate = formatter.format(OffsetDateTime.parse(headToHead.getDate()));
                headToHead.setFormattedDate(formattedDate);
            }

            model.addAttribute("headToHeadList", headToHeadList);
            return "head_to_head";
        } else {
            return "match_not_found";
        }
    }

//    @GetMapping("/lineups/{id}")
//    public String showLineups(@PathVariable("id") Long id, Model model) {
//        List<MainLineups> mainLineups = mainLineupsRepository.findByFixtureId(id);
//
//        if (!mainLineups.isEmpty()) {
//            Long teamId1 = mainLineups.get(0).getTeamId();
//
//            List<MainLineups> mainLineupsTeam1 = mainLineupsRepository.findByFixtureIdAndTeamId(id, teamId1);
//            List<StartXILineups> startXILineupsTeam1 = startXILineupsRepository.findByFixtureIdAndTeamId(id, teamId1);
//            List<SubstitutesLineups> substitutesLineupsTeam1 = substitutesLineupsRepository.findByFixtureIdAndTeamId(id, teamId1);
//
//            Long teamId2 = mainLineups.get(1).getTeamId();
//
//            List<MainLineups> mainLineupsTeam2 = mainLineupsRepository.findByFixtureIdAndTeamId(id, teamId2);
//            List<StartXILineups> startXILineupsTeam2 = startXILineupsRepository.findByFixtureIdAndTeamId(id, teamId2);
//            List<SubstitutesLineups> substitutesLineupsTeam2 = substitutesLineupsRepository.findByFixtureIdAndTeamId(id, teamId2);
//
//            model.addAttribute("mainLineupsTeam1", mainLineupsTeam1);
//            model.addAttribute("mainLineupsTeam2", mainLineupsTeam2);
//            model.addAttribute("startXILineupsTeam1", startXILineupsTeam1);
//            model.addAttribute("substitutesLineupsTeam1", substitutesLineupsTeam1);
//            model.addAttribute("startXILineupsTeam2", startXILineupsTeam2);
//            model.addAttribute("substitutesLineupsTeam2", substitutesLineupsTeam2);
//
//            return "lineups";
//        } else {
//            return "match_not_found";
//        }
//    }
@GetMapping("/lineups/{id}")
public String showLineups(@PathVariable("id") Long id, Model model) {
    List<MainLineups> mainLineups = mainLineupsRepository.findByFixtureId(id);

    if (!mainLineups.isEmpty()) {
        Long teamId1 = mainLineups.get(0).getTeamId();
        Long teamId2 = mainLineups.get(1).getTeamId();

        List<MainLineups> mainLineupsTeam1 = mainLineupsRepository.findByFixtureIdAndTeamId(id, teamId1);
        List<MainLineups> mainLineupsTeam2 = mainLineupsRepository.findByFixtureIdAndTeamId(id, teamId2);

        List<StartXILineups> startXILineupsTeam1 = startXILineupsRepository.findByFixtureIdAndTeamId(id, teamId1);
        List<StartXILineups> startXILineupsTeam2 = startXILineupsRepository.findByFixtureIdAndTeamId(id, teamId2);

        List<SubstitutesLineups> substitutesLineupsTeam1 = substitutesLineupsRepository.findByFixtureIdAndTeamId(id, teamId1);
        List<SubstitutesLineups> substitutesLineupsTeam2 = substitutesLineupsRepository.findByFixtureIdAndTeamId(id, teamId2);

        model.addAttribute("team1Name", mainLineupsTeam1.get(0).getTeamName());
        model.addAttribute("team1CoachName", mainLineupsTeam1.get(0).getCoachName());
        model.addAttribute("formationTeam1", mainLineupsTeam1.get(0).getFormation());
        model.addAttribute("goalkeeperBorderColorTeam1", mainLineupsTeam1.get(0).getGoalkeeperBorderColor());
        model.addAttribute("goalkeeperNumberColorTeam1", mainLineupsTeam1.get(0).getGoalkeeperNumberColor());
        model.addAttribute("playerBorderColorTeam1", mainLineupsTeam1.get(0).getPlayerBorderColor());
        model.addAttribute("playerNumberColorTeam1", mainLineupsTeam1.get(0).getPlayerNumberColor());
        model.addAttribute("team2Name", mainLineupsTeam2.get(0).getTeamName());
        model.addAttribute("team2CoachName", mainLineupsTeam2.get(0).getCoachName());
        model.addAttribute("formationTeam2", mainLineupsTeam2.get(0).getFormation());
        model.addAttribute("goalkeeperBorderColorTeam2", mainLineupsTeam2.get(0).getGoalkeeperBorderColor());
        model.addAttribute("goalkeeperNumberColorTeam2", mainLineupsTeam2.get(0).getGoalkeeperNumberColor());
        model.addAttribute("playerBorderColorTeam2", mainLineupsTeam2.get(0).getPlayerBorderColor());
        model.addAttribute("playerNumberColorTeam2", mainLineupsTeam2.get(0).getPlayerNumberColor());

        List<FieldPlayer> fieldPlayersTeam1 = createFieldPlayers(startXILineupsTeam1);
        List<FieldPlayer> fieldPlayersTeam2 = createFieldPlayers(startXILineupsTeam2);

        model.addAttribute("fieldPlayersTeam1", fieldPlayersTeam1);
        model.addAttribute("fieldPlayersTeam2", fieldPlayersTeam2);
        model.addAttribute("startXILineupsTeam1", startXILineupsTeam1);
        model.addAttribute("startXILineupsTeam2", startXILineupsTeam2);
        model.addAttribute("substitutesPlayersTeam1", substitutesLineupsTeam1);
        model.addAttribute("substitutesPlayersTeam2", substitutesLineupsTeam2);

        return "lineups";
    } else {
        return "match_not_found";
    }
}

    // Класс для представления данных игрока на поле
    public static class FieldPlayer {
        private int gridX;
        private int gridY;
        private int playerNumber;
        private int playerName;

        public int getGridX() {
            return gridX;
        }

        public void setGridX(int gridX) {
            this.gridX = gridX;
        }

        public int getGridY() {
            return gridY;
        }

        public void setGridY(int gridY) {
            this.gridY = gridY;
        }

        public int getPlayerNumber() {
            return playerNumber;
        }

        public int getPlayerName() {
            return playerName;
        }

        public void setPlayerNumber(int playerNumber) {
            this.playerNumber = playerNumber;
        }
    }

    // Метод для создания списка FieldPlayer из списка StartXILineups
    private List<FieldPlayer> createFieldPlayers(List<StartXILineups> startXILineupsList) {
        List<FieldPlayer> fieldPlayers = new ArrayList<>();
        for (StartXILineups startXILineups : startXILineupsList) {
            String[] gridValues = startXILineups.getPlayerGrid().split(":");
            int gridX = Integer.parseInt(gridValues[0]);
            int gridY = Integer.parseInt(gridValues[1]);

            FieldPlayer fieldPlayer = new FieldPlayer();
            fieldPlayer.setGridX(gridX);
            fieldPlayer.setGridY(gridY);
            fieldPlayer.setPlayerNumber(Integer.parseInt(startXILineups.getPlayerNumber()));

            fieldPlayers.add(fieldPlayer);
        }
        return fieldPlayers;
    }


//    @GetMapping("/lineups/{id}")
//    public String showLineups(@PathVariable("id") Long id, Long teamId, Model model) {
//        System.out.println("Fixture ID: " + id);
//        System.out.println("Team ID: " + teamId);
//
//        List<MainLineups> mainLineups = mainLineupsRepository.findByFixtureId(id);
//        List<StartXILineups> startXILineups = startXILineupsRepository.findByFixtureIdAndTeamId(id, teamId);
//        List<SubstitutesLineups> substitutesLineups = substitutesLineupsRepository.findByFixtureIdAndTeamId(id, teamId);
//
//        if (!mainLineups.isEmpty()) {
//            System.out.println("Main Lineups:");
//            for (MainLineups lineup : mainLineups) {
//                System.out.println(lineup);
//            }
//
//            System.out.println("Start XI Lineups:");
//            for (StartXILineups startXILineup : startXILineups) {
//                System.out.println(startXILineup);
//            }
//
//            System.out.println("Substitutes Lineups:");
//            for (SubstitutesLineups substitutesLineup : substitutesLineups) {
//                System.out.println(substitutesLineup);
//            }
//
//            model.addAttribute("mainLineups", mainLineups);
//            model.addAttribute("startXILineups", startXILineups);
//            model.addAttribute("substitutesLineups", substitutesLineups);
//            return "lineups";
//        } else {
//            return "match_not_found";
//        }
//    }
}

//@Controller
//public class FootballController {
//    @Autowired
//    private FixtureRepository fixtureRepository;
//
//    @GetMapping("/leagues")
//    @ResponseBody
//    @Cacheable(value = "fixturesCache", key = "'fixtures'", unless = "#result == null")
//    public Map<String, Object> leagues() {
//        String apiKey = "739fbef17c8cda1256722a1d0ae58ba3";
//
//        String endpoint = "fixtures?league=135&season=2023&from=2023-10-30&to=2023-11-05";
//
//        String apiUrl = "https://v3.football.api-sports.io/" + endpoint;
//
//        HttpResponse<JsonNode> response = Unirest.get(apiUrl)
//                .header("Accept", "application/json")
//                .header("x-rapidapi-key", apiKey)
//                .header("x-rapidapi-host", "v3.football.api-sports.io")
//                .asJson();
//
//        Map<String, Object> result = response.getBody().getObject().toMap();
//
//        List<Map<String, Object>> fixtures = (List<Map<String, Object>>) result.get("response");
//        for (Map<String, Object> fixtureData : fixtures) {
//            Map<String, Object> fixtureInfo = (Map<String, Object>) fixtureData.get("fixture");
//            Map<String, Object> homeTeam = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("teams")).get("home");
//            Map<String, Object> awayTeam = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("teams")).get("away");
//
//            Fixture fixture = new Fixture();
//            fixture.setId(Long.valueOf(String.valueOf(fixtureInfo.get("id"))));
//            fixture.setDate(String.valueOf(fixtureInfo.get("date")));
//            fixture.setHomeTeamName(String.valueOf(homeTeam.get("name")));
//            fixture.setAwayTeamName(String.valueOf(awayTeam.get("name")));
//
//            fixture.setReferee(String.valueOf(fixtureInfo.get("referee")));
//            Map<String, Object> fixtureStatus = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("fixture")).get("status");
//            if (fixtureStatus.get("long") != null) {
//                fixture.setStatus(String.valueOf(fixtureStatus.get("long")));
//            }
//
//            Map<String, Object> venue = (Map<String, Object>) ((Map<String, Object>) fixtureData.get("fixture")).get("venue");
//            if (venue.get("name") != null) {
//                fixture.setVenueName(String.valueOf(venue.get("name")));
//            }
//            if (venue.get("city") != null) {
//                fixture.setVenueCity(String.valueOf(venue.get("city")));
//            }
//
//            Map<String, Object> league = (Map<String, Object>) fixtureData.get("league");
//            fixture.setLeagueName(String.valueOf(league.get("name")));
//            fixture.setLeagueCountry(String.valueOf(league.get("country")));
//            fixture.setLeagueLogo(String.valueOf(league.get("logo")));
//            fixture.setLeagueFlag(String.valueOf(league.get("flag")));
//            fixture.setHomeTeamLogo(String.valueOf(homeTeam.get("logo")));
//
//            if (homeTeam.get("winner") != null) {
//                fixture.setHomeTeamWinner(String.valueOf(homeTeam.get("winner").toString()));
//            }
//            fixture.setAwayTeamLogo(String.valueOf(awayTeam.get("logo")));
//            if (awayTeam.get("winner") != null) {
//                fixture.setAwayTeamWinner(String.valueOf(awayTeam.get("winner").toString()));
//            }
//
//            Map<String, Object> goals = (Map<String, Object>) fixtureData.get("goals");
//            if (goals != null) {
//                if (goals.get("home") != null) {
//                    fixture.setHomeGoals(Integer.parseInt(goals.get("home").toString()));
//                }
//
//                if (goals.get("away") != null) {
//                    fixture.setAwayGoals(Integer.parseInt(goals.get("away").toString()));
//                }
//            }
//
//            fixtureRepository.save(fixture);
//        }
//
//        return result;
//    }
//
//    @GetMapping("/matches")
//    public String showMatches(Model model) {
//        List<Fixture> matches = fixtureRepository.findAll();
//        model.addAttribute("matches", matches);
//        return "matches";
//    }
//
//    @GetMapping("/match/{id}")
//    public String showMatchDetails(@PathVariable("id") Long id, Model model) {
//        Optional<Fixture> match = fixtureRepository.findById(id);
//        if (match.isPresent()) {
//            model.addAttribute("match", match.get());
//            return "match_details";
//        } else {
//            return null;
//        }
//    }
//
//    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
//    public void leaguesScheduledUpdate() {
//        leagues();
//    }
//}
