behaviorList = new Behavior[] {
    new ForwardBehavior(),                 // Behavior 0: Lowest priority (constant forward motion)
    new UltrasoundBehavior(),              // Behavior 1: Ultrasound-based wall tracking
    new SoundBehavior(),                   // Behavior 2: Responds to sound events
    new LightBehavior(),                   // Behavior 3: Adjusts based on sound threshold
    new TouchBehavior(),                   // Behavior 4: Touch-triggered reaction with P-control
    new ButtonBehavior()                   // Behavior 5: Highest priority (terminates on button press)
};
