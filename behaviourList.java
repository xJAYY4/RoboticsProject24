behaviorList = new Behavior[]{
    new MoveForward(engineR, engineL),         // Lowest priority
    new UltrasoundBehavior(ultrasonicSensor, engineR, engineL),
    new SoundBehavior(engineR, engineL, soundSensor),
    new LightBehavior(ultrasonicSensor),
    new TouchBehavior(touchSensor, engineR, engineL),
    new ButtonBehavior()                       // Highest priority
};
