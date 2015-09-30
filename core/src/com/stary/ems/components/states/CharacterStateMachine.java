/**
 * @author billzhu
 */
package com.stary.ems.components.states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;

public class CharacterStateMachine extends DefaultStateMachine<Entity>{

	public CharacterStateMachine(Entity owner) {
		super(owner);
	}

}
