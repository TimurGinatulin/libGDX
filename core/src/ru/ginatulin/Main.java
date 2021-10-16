package ru.ginatulin;

import com.badlogic.gdx.Game;

import ru.ginatulin.screen.MainScreen;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new MainScreen());
    }
}
