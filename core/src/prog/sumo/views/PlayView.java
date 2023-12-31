package prog.sumo.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import prog.sumo.controllers.PlayerController;
import prog.sumo.models.Character;
import prog.sumo.models.Player;
import prog.sumo.models.WrestleRing;

public final class PlayView extends View {
    private static final int MAX_ROUNDS = 3;
    public static int battleCircleRadius = Gdx.graphics.getWidth() / 2 + 20;
    public static int startPositionOfPlayer1, startPositionOfPlayer2;
    private final float countdownTime = 3f; // Countdown time in seconds
    private final float countdownStartTime;
    private float timeElapsed = 0f; // Time elapsed since countdown started
    private final WrestleRing wrestleRing;
    private final Player player1;
    private final Player player2;
    private final PlayerController playerController;
    private BitmapFont font; // Font to draw the countdown
    private BitmapFont fontForScore; // Font to draw the countdown
    Texture settingsWheel, windowTex, backTex, quitTex, tutorialTex, tutWinTex,
            writtenTutTex, back2Tex;
    Texture hand1Tex, hand2Tex;
    ShapeRenderer shapeRenderer;
    Drawable settingsWheelDrawable, windowDraw, backDraw, quitDraw,
            tutorialDraw, tutWinDraw, writtenTutDraw, back2Draw;
    Drawable player1Drawable, player2Drawable;
    ImageButton settingsB, quitB, backB, tutB, writtenTutB, back2B;
    ImageButton hand1, hand2;
    Stage stage;

    Window pinkWindow, orangeWindow;
    // The following should be changed to Player objects when that part is ready
    Player winnerOfTheRound = null;
    Player winnerOfTheGame = null;
    int roundCounter;
    boolean isGameOver;

    public PlayView(GameViewManager gvm, Character player1Character,
                    Character player2Character) {

        super(gvm);
        shapeRenderer = new ShapeRenderer();
        settingsWheel = new Texture("settingswheel.png");
        hand1Tex = new Texture("greenhand.png");
        hand2Tex = new Texture("purplehand.png");
        windowTex = new Texture("pinkWindow.png");
        quitTex = new Texture("quitGame.png");
        backTex = new Texture("backToGame.png");
        tutorialTex = new Texture("tutorialOrangeButton.png");
        tutWinTex = new Texture("orangeWindow.png");
        writtenTutTex = new Texture("writtenTutorial.png");
        back2Tex = new Texture("back2.png");

        settingsWheelDrawable = new TextureRegionDrawable(settingsWheel);
        player1Drawable = new TextureRegionDrawable(hand1Tex);
        player2Drawable = new TextureRegionDrawable(hand2Tex);
        windowDraw = new TextureRegionDrawable(windowTex);
        quitDraw = new TextureRegionDrawable(quitTex);
        backDraw = new TextureRegionDrawable(backTex);
        tutorialDraw = new TextureRegionDrawable(tutorialTex);
        tutWinDraw = new TextureRegionDrawable(tutWinTex);
        writtenTutDraw = new TextureRegionDrawable(writtenTutTex);
        back2Draw = new TextureRegionDrawable(back2Tex);

        settingsB = new ImageButton(settingsWheelDrawable);
        quitB = new ImageButton(quitDraw);
        quitB.setTransform(true);
        quitB.setScale(2f);
        backB = new ImageButton(backDraw);
        backB.setTransform(true);
        backB.setScale(2f);
        tutB = new ImageButton(tutorialDraw);
        tutB.setTransform(true);
        tutB.setScale(2f);
        hand1 = new ImageButton(player1Drawable);
        hand2 = new ImageButton(player2Drawable);
        writtenTutB = new ImageButton(writtenTutDraw);
        writtenTutB.setTransform(true);
        writtenTutB.setScale(2f);
        back2B = new ImageButton(back2Draw);
        back2B.setTransform(true);
        back2B.setScale(2f);

        wrestleRing = new WrestleRing(Gdx.graphics.getWidth() / 2 + 20);

        playerController = new PlayerController();
        player1 = new Player(player1Character, 0);
        player2 = new Player(player2Character, 1);

        roundCounter = 1;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(settingsB);
        stage.addActor(hand1);
        stage.addActor(hand2);
        startPositionOfPlayer1 =
                Gdx.graphics.getHeight() / 2 + wrestleRing.getRadius()
                        - player1.getCharacter().getTexture().getHeight();
        startPositionOfPlayer2 =
                Gdx.graphics.getHeight() / 2 - wrestleRing.getRadius();
        player1.setPosition(startPositionOfPlayer1);
        player2.setPosition(startPositionOfPlayer2);


        settingsB.setPosition(Gdx.graphics.getWidth() - settingsB.getWidth(),
                Gdx.graphics.getHeight() / 2f - settingsB.getHeight() / 2);
        hand1.setPosition(Gdx.graphics.getWidth() / 2f - hand1.getWidth() / 2,
                0);
        hand2.setPosition(Gdx.graphics.getWidth() / 2f - hand2.getWidth() / 2,
                Gdx.graphics.getHeight() - hand2.getHeight());
        isGameOver = false;
        countdownStartTime = 0f;
        settingsB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleInput("settingsB");
            }
        });
        quitB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleInput("quitB");
            }
        });

        backB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleInput("backB");
            }
        });
        tutB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleInput("tutB");
            }
        });


        hand1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleInput("player1");
            }
        });

        hand2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleInput("player2");
            }
        });
        back2B.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleInput("back2");
            }
        });
        pinkWindow = new Window("Settings",
                new Window.WindowStyle(new BitmapFont(), new Color(0, 0, 0, 0),
                        windowDraw)) {
            public void result(Object obj) {
                System.out.println("result " + obj);
            }
        };
        orangeWindow = new Window("Tutorial",
                new Window.WindowStyle(new BitmapFont(), new Color(0, 0, 0, 0),
                        tutWinDraw)) {
            public void result(Object obj) {
                System.out.println("result " + obj);
            }
        };

        Table table = new Table();
        // Add buttons to the table
        table.add(tutB).padRight(Gdx.graphics.getWidth() / 2f + 30);
        table.row().padTop(200); // Create a new row
        table.add(backB).padRight(Gdx.graphics.getWidth() / 2f + 30);
        table.row().padTop(185); // Create a new row
        table.add(quitB).padRight(Gdx.graphics.getWidth() / 2f + 30);

        pinkWindow.setResizable(true);
        pinkWindow.add(table);
        pinkWindow.pack();

        Table table2 = new Table();

        table2.add(writtenTutB).padRight(Gdx.graphics.getWidth() / 2f + 40)
                .padTop(550);
        table2.row().padTop(300);
        table2.add(back2B).padRight(Gdx.graphics.getWidth() / 2f + 30);

        //table2.add(back2B).padRight(Gdx.graphics.getWidth()/ 2 +30);
        orangeWindow.add(table2);
        orangeWindow.pack();

    }

    @Override
    protected void handleInput(String name) {
        switch (name) {
            case "settingsB":
                stage.addActor(pinkWindow);
                break;
            case "player1":
                //Calling the movePlayer method from the Player class
                playerController.movePlayer(player2, player1);
                isRoundOver();
                break;
            case "player2":
                //Calling the movePlayer method from the Player class
                playerController.movePlayer(player1, player2);
                isRoundOver();
                break;
            default:
                // handle invalid input
                break;
            case "tutB":

                stage.addActor(orangeWindow);
                break;
            case "quitB":
                gvm.set(new MainMenuView(gvm));
                break;
            case "backB":
                stage.addAction(Actions.removeActor(pinkWindow));
                break;
            case "back2":
                stage.addAction(Actions.removeActor(orangeWindow));
                break;
        }
    }

    /**
     * Checks if the round is over. This method can be safely extended by
     * subclasses.
     */
    public void isRoundOver() {

        //If player 1 is out of the battle circle
        if (player1.getPosition()
                - player1.getCharacter().getTexture().getHeight() / 2
                > startPositionOfPlayer1) {
            //Increment score for player 2
            whenRoundFinished(player2);
        } else if (player2.getPosition()
                + player2.getCharacter().getTexture().getHeight() / 2
                < startPositionOfPlayer2) {
            //Increment score for player 1
            whenRoundFinished(player1);
        }
    }

    @Override
    public void update(float dt) {
        stage.act();
    }

    private void incrementScoreOfWinner() {
        if (winnerOfTheRound.equals(player1)) {
            player1.incrementScore();
        } else {
            player2.incrementScore();
        }
    }

    // The following method should be used when a round is finished
    public void whenRoundFinished(Player winner) {
        incrementRoundCounter();
        this.winnerOfTheRound = winner;
        incrementScoreOfWinner();
        checkIfGameIsFinished();
        player1.setPosition(startPositionOfPlayer1);
        player2.setPosition(startPositionOfPlayer2);
    }

    private void incrementRoundCounter() {
        roundCounter++;
    }

    public void checkIfGameIsFinished() {
        // to avoid running unneccessary code in simplest code
        if (roundCounter == MAX_ROUNDS) {
            isGameOver = true;
        } else {
            // if either player has higher score than half
            // of max rounds, the game is over
            int breakpoint = (int) Math.floor(MAX_ROUNDS / 2f);
            if (player1.getScore() > breakpoint) {
                isGameOver = true;
                winnerOfTheGame = player1;
                whenGameIsFinished();
            } else if (player2.getScore() > breakpoint) {
                winnerOfTheGame = player2;
                isGameOver = true;
                whenGameIsFinished();
            }
        }
    }

    public void whenGameIsFinished() {
        if (isGameOver) {
            gvm.set(new WinnerView(gvm, winnerOfTheGame));
        }
    }

    private void drawScore(SpriteBatch sb) {
        Matrix4 originalMatrix =
                sb.getTransformMatrix().cpy(); // Save the original matrix
        sb.begin();
        sb.setTransformMatrix(new Matrix4().setToRotation(0, 0, 1, 90));
        if (fontForScore == null) {
            fontForScore = new BitmapFont();
            fontForScore.getData().setScale(5f);
        }
        fontForScore.setColor(Color.BLACK);
        fontForScore.draw(sb, "" + player1.getScore(),
                Gdx.graphics.getWidth() + (fontForScore.getCapHeight() * 2),
                -fontForScore.getCapHeight());
        fontForScore.draw(sb, "-",
                Gdx.graphics.getWidth() + (fontForScore.getCapHeight()),
                -fontForScore.getCapHeight());
        fontForScore.draw(sb, "" + player2.getScore(), Gdx.graphics.getWidth(),
                -fontForScore.getCapHeight());
        sb.end();
        sb.setTransformMatrix(originalMatrix); // Restore the original matrix

    }

    private void drawGame(SpriteBatch sb) {
        Gdx.gl.glClearColor(252 / 255f, 231 / 255f, 239 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.circle(Gdx.graphics.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f,
                wrestleRing.getRadius());
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(255 / 255f, 236 / 255f, 136 / 255f, 1);
        shapeRenderer.circle(Gdx.graphics.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f,
                Gdx.graphics.getWidth() / 2f - 70);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.circle(Gdx.graphics.getWidth() + 10,
                Gdx.graphics.getHeight() / 2f, 160);
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.circle(-10, Gdx.graphics.getHeight() / 2f, 160);
        shapeRenderer.end();
        drawScore(sb);
        sb.begin();
        sb.draw(player1.getCharacter().getTexture(),
                Gdx.graphics.getWidth() / 2f
                        - player1.getCharacter().getTexture().getWidth() / 2f,
                player1.getPosition());
        sb.draw(player2.getCharacter().getTexture(),
                Gdx.graphics.getWidth() / 2f
                        - player2.getCharacter().getTexture().getWidth() / 2f,
                player2.getPosition());
        sb.end();
        stage.draw();
    }

    private void showCountdown(SpriteBatch sb) {
        float countdownEndtime = countdownStartTime + countdownTime;
        // Draw game in background
        drawGame(sb);

        // Draw the background and overlay
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.69f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        shapeRenderer.end();

        // Update the countdown timer
        timeElapsed += Gdx.graphics.getDeltaTime();
        // Draw the countdown
        int countdownNumber = (int) (countdownEndtime - timeElapsed) + 1;
        if (font == null) {
            font = new BitmapFont();
            font.getData().setScale(12f);
        }
        sb.begin();
        font.draw(sb, Integer.toString(countdownNumber),
                Gdx.graphics.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f);
        sb.end();
    }

    @Override
    public void render(SpriteBatch sb) {
        drawGame(sb);
        if (timeElapsed < countdownTime) {
            showCountdown(sb);
        }
    }

    @Override
    public void dispose() {
        settingsWheel.dispose();
        hand1Tex.dispose();
        hand2Tex.dispose();
        player1.getCharacter().getTexture().dispose();
        player2.getCharacter().getTexture().dispose();
        stage.dispose();
    }

}
