package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class PovUpTrigger extends Trigger {
  private final Joystick gamepad;

  public PovUpTrigger(Joystick gamepad) {
    this.gamepad = gamepad;
  } 

  @Override
  public boolean get() {
    // This returns whether the trigger is active
    int povAngle = gamepad.getPOV();
    return ( povAngle==0 || povAngle==315 || povAngle==45 );
  }
}