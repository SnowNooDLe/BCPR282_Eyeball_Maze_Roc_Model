package dos0311.ara.ac.nz.eyeballmaze_rocs_model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dos0311.ara.ac.nz.eyeballmaze_rocs_model.Model.*;

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity {
//  Controller for Roc's MODEL

    Switch soundOnOffSwitch;
    MediaPlayer bgm = null, lost_case_sound, won_case_sound;
    ImageView[][] imageViews = new ImageView[6][4];
    int[][] imageSrcs = new int[6][4];
    TextView textViewForGoal;
    TextView textViewForMovements;
    TextView textViewForStage;
    //  Time to launch the another activity
    private static int TIME_OUT = 1500;
    //    It means game is not finished yet if it is true.
    private Boolean gameIsOn;
    //    will be used for task 17, if user keep trying bad thing, popup warning with rule.
    private int errorCount = 0;
    private int currRow;
    private int currCol;
    private int currentStage;
    private int targetRow;
    private int targetCol;
    private Piece theEndPiece, thePiece, theStartPiece;
    private Eyeball eb;
    private GameGridIron gr;

//    Roc's model does not contain number of goal variable neither set/getter, allocating manually
    private int goal;

//    My previous
    private int rowSize = 6;
    private int colSize = 4;


    //    for loading & saving game
    private int currentNumOfMovements, currentNumOfGoals, currentEyeballRowPosition, currentEyeballColPosition;
    private String currentDirection;
    private Point[] currentMovementHistry;
    private String[] currentDirectionHistory;
    private boolean gameIsSaved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Task 3, Manually create a GUI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Task 14, Display a GUI element to control sound on / off
        soundOnOffSwitch = findViewById(R.id.switchSoundOnOff);
        bgm = MediaPlayer.create(MainActivity.this, R.raw.hypnotic_puzzle3);
        soundOnOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    bgm = MediaPlayer.create(MainActivity.this,R.raw.hypnotic_puzzle3);
                    bgm.start();
                } else {
                    // The toggle is disabled
                    bgm.stop();
                    bgm.release();
                }
            }
        });

//        for the goal textView
        textViewForGoal = findViewById(R.id.textViewGoals);
        textViewForMovements = findViewById(R.id.textViewMovements);
        textViewForStage = findViewById(R.id.stageIndicator);
        gameIsSaved = false;

        startGame();
    }
    //  Extra View Feature 3, user can select level by clicking this.
    public void stageOneSetup(){
//        when start new game, always reset the number of goal
        goal = 0;
        gameIsSaved = false;
        for (int i = 0; i < rowSize; i++){
            for (int j = 0; j < colSize; j++){
                String imageview = "imageView" + i + j;
                int resID = getResources().getIdentifier(imageview, "id", getPackageName());
                imageViews[i][j] = findViewById(resID);
                imageSrcs[i][j] = R.drawable.empty_block;
            }
        }
        //      first row
        imageSrcs[0][2] = R.drawable.red_flower;

        //        second row
        imageSrcs[1][0] = R.drawable.cyan_cross;
        imageSrcs[1][1] = R.drawable.yellow_flower;
        imageSrcs[1][2] = R.drawable.yellow_diamond;
        imageSrcs[1][3] = R.drawable.green_cross;

        //      Third row
        imageSrcs[2][0] = R.drawable.green_flower;
        imageSrcs[2][1] = R.drawable.red_star;
        imageSrcs[2][2] = R.drawable.green_star;
        imageSrcs[2][3] = R.drawable.yellow_diamond;

        //      fourth row
        imageSrcs[3][0] = R.drawable.red_flower;
        imageSrcs[3][1] = R.drawable.cyan_flower;
        imageSrcs[3][2] = R.drawable.red_star;
        imageSrcs[3][3] = R.drawable.green_flower;

        //        Fifth row
        imageSrcs[4][0] = R.drawable.cyan_star;
        imageSrcs[4][1] = R.drawable.red_diamond;
        imageSrcs[4][2] = R.drawable.cyan_flower;
        imageSrcs[4][3] = R.drawable.cyan_diamond;

        //        Sixth row
        imageSrcs[5][1] = R.drawable.cyan_diamond;

//      resetting the displayed image to new stage
        for (int i = 0; i < rowSize; i++){
            for (int j = 0; j < colSize; j++){
                imageViews[i][j].setImageBitmap(BitmapFactory.decodeResource(getResources(), imageSrcs[i][j]));
            }
        }
        //        for image
        setGoalInMaze(0,2);
        setPlayerInMaze(5,1);
        goal++;

        gr = new GameGridIron(0);
        eb = new Eyeball (gr);
        currentStage = 1;
    }

    public void stageTwoSetup(){
//        when start new game, always reset the number of goal
        goal = 0;
        gameIsSaved = false;
        for (int i = 0; i < rowSize; i++){
            for (int j = 0; j < colSize; j++){
                String imageview = "imageView" + i + j;
                int resID = getResources().getIdentifier(imageview, "id", getPackageName());
                imageViews[i][j] = findViewById(resID);
                imageSrcs[i][j] = R.drawable.empty_block;
            }
        }

//      allocate images into imageSrces
        imageSrcs[0][2] = R.drawable.red_flower;

        imageSrcs[1][0] = R.drawable.cyan_cross;
        imageSrcs[1][1] = R.drawable.cyan_flower;
        imageSrcs[1][2] = R.drawable.cyan_diamond;
        imageSrcs[1][3] = R.drawable.green_cross;

        imageSrcs[2][0] = R.drawable.green_flower;
        imageSrcs[2][1] = R.drawable.red_star;
        imageSrcs[2][2] = R.drawable.green_star;
        imageSrcs[2][3] = R.drawable.yellow_flower;

        imageSrcs[3][0] = R.drawable.red_flower;
        imageSrcs[3][1] = R.drawable.green_diamond;
        imageSrcs[3][2] = R.drawable.red_star;
        imageSrcs[3][3] = R.drawable.yellow_star;

        imageSrcs[4][0] = R.drawable.green_cross;
        imageSrcs[4][1] = R.drawable.red_diamond;
        imageSrcs[4][2] = R.drawable.cyan_flower;
        imageSrcs[4][3] = R.drawable.green_diamond;

        imageSrcs[5][1] = R.drawable.cyan_diamond;

//        resetting the displayed image to new stage
        for (int i = 0; i < rowSize; i++){
            for (int j = 0; j < colSize; j++){
                imageViews[i][j].setImageBitmap(BitmapFactory.decodeResource(getResources(), imageSrcs[i][j]));
            }
        }

//      for image
        setGoalInMaze(0,2);
        setPlayerInMaze(5,1);
        goal++;

        gr = new GameGridIron(1);
        eb = new Eyeball (gr);
        currentStage = 2;
    }

    //    So music will be paused when back button is pressed
    @Override
    public void onBackPressed ()
    {
        if (bgm != null)
            bgm.stop();
        super.onBackPressed();
    }

    @Override
    public void onPause ()
    {
        if (bgm != null)
        {
            bgm.pause();
            bgm.stop();
        }
        super.onPause();
    }

    //    Starting the game with stage 1 but when it starts, always run stage one first
    public void startGameStageOne() {
        gameIsOn = true;
        currentStage = 1;
//        Extra View Feature 4, stage indicator
        textViewForStage.setText(R.string.stage_one);
        stageOneSetup();
        setTextForGoalMovement();
    }
    //    Starting the game with stage 2
    public void startGameStageTwo(){
        gameIsOn = true;
        currentStage = 2;
        //        Extra View Feature 4, stage indicator
        textViewForStage.setText(R.string.stage_two);
        stageTwoSetup();
        setTextForGoalMovement();
    }

    //    Extra View Feature 3, choose stages via button click
    public void startGameStageOneViaButton(View view){
        startGameStageOne();
    }

    public void startGameStageTwoViaButton(View view){
        startGameStageTwo();
    }

    //    Starting the game and always start with Stage 1.
    public void startGame() {
//        Controller for Roc's Code
        gameIsOn = true;

        stageOneSetup();

//        Goal point
        theEndPiece = gr.findEndPointPiece();

        setTextForGoalMovement();

    }
    public void setTextForGoalMovement(){
        //      Task 12. Display the number of goals to do
        textViewForGoal.setText("Number of Goal(s) : " + goal);
//       Task 13. Display move counts
        textViewForMovements.setText("Number of Movements : " + eb.countTotalMove());
    }

    //    Task 5, Display image of player character
    private void setPlayerInMaze(int row, int col) {
        Bitmap image1 = BitmapFactory.decodeResource(getResources(), imageSrcs[row][col]);
        Bitmap image2 = BitmapFactory.decodeResource(getResources(), R.drawable.eyeball_up);
        Bitmap mergedImages = createSingleImageFromMultipleImages(image1, image2);
        imageViews[row][col].setImageBitmap(mergedImages);
    }

    //        Task 6, Display Goal(s)
    private void setGoalInMaze(int row, int col) {
        Bitmap image1 = BitmapFactory.decodeResource(getResources(), imageSrcs[row][col]);
        Bitmap image2 = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        Bitmap mergedImages = createSingleImageFromMultipleImages(image1, image2);
        imageViews[row][col].setImageBitmap(mergedImages);
    }

    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {
        Bitmap result = Bitmap.createBitmap(secondImage.getWidth(), secondImage.getHeight(), secondImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 10, 10, null);
        return result;
    }


    private String getLocationImageView(ImageView imageView) {
        // https://stackoverflow.com/questions/10137692/how-to-get-resource-name-from-resource-id
        String name = getResources().getResourceEntryName(imageView.getId());
        name = name.replace("imageView", "");
        return name;
    }

    public void onClickToMove(View view) {
        if (checkGameIsOver()){
            ImageView nextImageView = (ImageView) view;

            String targetPosition = getLocationImageView(nextImageView);
//        to get row and col values
            targetRow = Character.digit(targetPosition.charAt(0), 10);
            targetCol = Character.digit(targetPosition.charAt(1), 10);

            thePiece = eb.getMyCurrentPiece();

//            As it's Roc's model but he didnt have getter, so had to chance protected to public.
            currRow = thePiece.x;
            currCol = thePiece.y;

            if (eb.canDoNextMove(targetRow, targetCol)){
//            Resetting current spot's image
                imageViews[currRow][currCol].setImageBitmap(BitmapFactory.decodeResource(getResources(), imageSrcs[currRow][currCol]));

//                imageViews[targetRow][targetCol].setImageBitmap(BitmapFactory.decodeResource(getResources(), imageSrcs[targetRow][targetCol]));
                eb.moveToNextPieceSucceed(targetRow, targetCol);

                movementHappening(targetRow, targetCol);

//        updating movements display
                textViewForMovements.setText("Number of Movements : " + eb.countTotalMove());
            } else {
//                just telling what to do
                if (errorCount < 3){
                    warningMSG("You cannot move to there.");
//                    Task 17, showing corresponding rule
                } else {
                    warningMSG("Based on the face of Eyeball, you can only go to your Front, Right or Left. And the tile you want to go, must be either same color or same shape to your Eyeball's current tile.");
                }
                errorCount++;
            }
//            debugging purpose
            Log.d("MYINT", "what is the EndPoint X: " + theEndPiece.x);
            Log.d("MYINT", "what is the EndPoint Y: " + theEndPiece.y);
//            theEndPiece is the goal.
            if (targetRow == theEndPiece.x && targetCol == theEndPiece.y){
//               Task 15, playing winning song
                won_case_sound = MediaPlayer.create(MainActivity.this,R.raw.won_sound);
                won_case_sound.start();
//                textViewForGoal.setText("Number of Goal(s) : " + "0");
                gameFinishedMSG("Congratulations ! You won the game !");
                gameIsOn = false;
            }

//           checking movements to decide whether game should be over or not
            if (eb.countTotalMove() > 10){
//               Task 16, playing lost sound
                lost_case_sound = MediaPlayer.create(MainActivity.this,R.raw.lost_sound);
                lost_case_sound.start();
                gameIsOverBadEnding();
            }
        } else {
            warningMSG("Game is finished, please restart the game if you want");
        }
    }

    //    one for button, one for dialog
    public void resetCurrentStageForButton(View view){
        resetStage();
    }

    public void resetCurrentStageForDialog(){
        resetStage();
    }

    //    Task 7, Button for restarting the current maze
    private void resetStage(){
//        resetting previous eyeball image
        gameIsOn = true;
        goal = 1;
        imageViews[targetRow][targetCol].setImageBitmap(BitmapFactory.decodeResource(getResources(), imageSrcs[targetRow][targetCol]));
        if (currentStage == 1){
            gr = new GameGridIron(0);
        } else if (currentStage == 2){
            gr = new GameGridIron(1);
        }

        eb = new Eyeball (gr);
        theEndPiece = gr.findEndPointPiece();
        setPlayerInMaze(5, 1);
        setGoalInMaze(0,2);

        textViewForGoal.setText("Number of Goal(s) : " + goal);
        textViewForMovements.setText("Number of Movements : " + eb.countTotalMove());
    }

    //    Task 18 & Task 20,  Display dialogue with options after player character has lost
    public void gameIsOverBadEnding(){
        gameIsOn = false;
        gameFinishedMSG("You couldn't make it within 10 movements, please try again !");
    }

    private void warningMSG(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    //    Task 18 & Task 19,  Display dialogue with options after player character has won
//    as its combined for Task 18 ~ 20, depends on the message, can be for won or lost
    private void gameFinishedMSG(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
//      Local variable just for this dialog
        String title;

        //      As I only designed for first two stages,
//      when it gets to second stage, only option will be restart
        if (currentStage == 1){
            title = "Go to stage 2";
        } else {
            title = "Restart !";
        }

        alertDialogBuilder.setPositiveButton(title,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (currentStage == 1){
                            startGameStageTwo();

                        } else{
                            resetCurrentStageForDialog();
                        }

                    }
                });

        alertDialogBuilder.setNegativeButton("Close",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //    Extra view feature 1, go back one movement
    public void goBackOneMove(View view){
        if (checkGameIsOver()){

            int movementsNum = eb.getMyAllMovedPieces().size();
            //        resetting current image without user.
            int currentRow = eb.myAllMovedPieces.get(movementsNum-1).x;
            int currentCol = eb.myAllMovedPieces.get(movementsNum-1).y;
//            compare whether player is at starting point
            if (eb.isReachingStartPoint()){
                warningMSG("You are at starting point, cannot go back.");
            } else {
                imageViews[currentRow][currentCol].setImageBitmap(BitmapFactory.decodeResource(getResources(), imageSrcs[currentRow][currentCol]));
                //        go back one movement which will decrease number of movement, position as well

                int previousRow = eb.myAllMovedPieces.get(movementsNum-2).x;
                int previousCol = eb.myAllMovedPieces.get(movementsNum-2).y;

                eb.moveBackPreviousPieceSucceed(previousRow, previousCol);
                movementHappening(previousRow, previousCol);

                //      update the number of movements as well
                textViewForMovements.setText("Number of Movements : " + eb.countTotalMove());
            }
        } else {
            warningMSG("It only works when game is not finished");
        }
    }

    private Boolean checkGameIsOver(){
        return gameIsOn;
    }

    //    Task 2, Button for saving a maze
    public void saveCurrentGame(View view){
        if (checkGameIsOver()){
            if (eb.isReachingStartPoint()){
                warningMSG("You are still at starting point, no need to save");
            } else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference goals = database.getReference("goals");
                goals.setValue(goal);

                DatabaseReference directions = database.getReference("directions");
                directions.setValue(eb.getMyCurrentDirection());

                DatabaseReference currentRowPosition = database.getReference("currentRowPosition");
                currentRowPosition.setValue(eb.getMyCurrentPiece().x);

                DatabaseReference currentColPosition = database.getReference("currentColPosition");
                currentColPosition.setValue(eb.getMyCurrentPiece().y);


                DatabaseReference movementDirectionHistory = database.getReference("movementDirectionHistory");
                movementDirectionHistory.setValue(eb.getMyAllMovedPieces());


                gameIsSaved = true;
            }


        } else {
            warningMSG("Game is finished :) No need to save");
        }
    }
    public void loadingDataFromDB(){
        if (currentStage == 1){
            gr = new GameGridIron(0);
        } else if (currentStage == 2){
            gr = new GameGridIron(1);
        }

        eb = new Eyeball (gr);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

//            For number of goals
        DatabaseReference goals = database.getReference("goals");
        // Attach a listener to read the data at our posts reference
        goals.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer goalsFromDB = dataSnapshot.getValue(Integer.class);
                goal = goalsFromDB;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
//            For Direction
        DatabaseReference directions = database.getReference("directions");
        // Attach a listener to read the data at our posts reference
        directions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Direction directionsFromDB = dataSnapshot.getValue(Direction.class);
                eb.myCurrentDirection = directionsFromDB;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
//            For Row Position
        DatabaseReference currentRowPosition = database.getReference("currentRowPosition");
        // Attach a listener to read the data at our posts reference
        currentRowPosition.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer currentRowPositionFromDB = dataSnapshot.getValue(Integer.class);
                eb.myCurrentPiece.x = currentRowPositionFromDB;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
//            For Col Position
        DatabaseReference currentColPosition = database.getReference("currentColPosition");
        // Attach a listener to read the data at our posts reference
        currentColPosition.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer currentColPositionFromDB = dataSnapshot.getValue(Integer.class);
                eb.myCurrentPiece.y = currentColPositionFromDB;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

//            movement & direction history
        DatabaseReference movementDirectionHistory = database.getReference("movementDirectionHistory");
        // Attach a listener to read the data at our posts reference
        movementDirectionHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Integer newX = ds.child("x").getValue(Integer.class);
                    Integer newY = ds.child("y").getValue(Integer.class);
                    if (!ds.child("startPoint").getValue(Boolean.class)){
                        Log.d("MYINT", "What is the value from DB X: " + newX);
                        Log.d("MYINT", "What is the value from DB Y: " + newY);
                        eb.moveToNextPieceSucceed(newX, newY);
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void restSetupAfterLoad(){
//            actual movement happening,
        movementHappening(eb.myCurrentPiece.x, eb.myCurrentPiece.y);

//        update the number of movements & Goal as well
        textViewForMovements.setText("Number of Movements: " + eb.countTotalMove());
        textViewForGoal.setText("Number of Goal(s): " + goal);

    }

    //    Task 1, Button for loading a maze
    public void loadCurrentGame(View view){
        if (checkGameIsOver() && gameIsSaved){
            thePiece = eb.getMyCurrentPiece();

//            As it's Roc's model but he didnt have getter, so had to chance protected to public.
            currRow = thePiece.x;
            currCol = thePiece.y;
            //        resetting current image without user.
            imageViews[currRow][currCol].setImageBitmap(BitmapFactory.decodeResource(getResources(), imageSrcs[currRow][currCol]));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    restSetupAfterLoad();
                }
            }, TIME_OUT);

            loadingDataFromDB();
        } else {
//          EXTRA VIEW Feature 4, warning sign when it is not during the came or never been saved.
            warningMSG("You can only load the game when its not finished or been saved before");
        }
    }

    public void movementHappening(int targetRow, int targetCol){
        Bitmap image1 = BitmapFactory.decodeResource(getResources(), imageSrcs[targetRow][targetCol]);
        Bitmap image2 = null;

        switch (eb.getMyCurrentDirection()){
            case NORTH:
                image2 = BitmapFactory.decodeResource(getResources(), R.drawable.eyeball_up);
                break;
            case WEST:
                image2 = BitmapFactory.decodeResource(getResources(), R.drawable.eyeball_left);
                break;
            case SOUTH:
                image2 = BitmapFactory.decodeResource(getResources(), R.drawable.eyeball_down);
                break;
            case EAST:
                image2 = BitmapFactory.decodeResource(getResources(), R.drawable.eyeball_right);
                break;
        }

        Bitmap mergedImages = createSingleImageFromMultipleImages(image1, image2);
        imageViews[targetRow][targetCol].setImageBitmap(mergedImages);
    }
}
