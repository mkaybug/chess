package model.response;

import model.GameData;

import java.util.Collection;

public record GamesResponse(Collection<GameData> games) {
}
