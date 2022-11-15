package mindustry.entities.abilities;

import arc.util.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;

import static mindustry.Vars.tilesize;
import static mindustry.arcModule.RFuncs.abilityPro;

public class ShieldRegenFieldAbility extends Ability{
    public float amount = 1, max = 100f, reload = 100, range = 60;
    public Effect applyEffect = Fx.shieldApply;
    public Effect activeEffect = Fx.shieldWave;
    public boolean parentizeEffects;

    protected float timer;
    protected boolean applied = false;

    ShieldRegenFieldAbility(){}

    public ShieldRegenFieldAbility(float amount, float max, float reload, float range){
        this.amount = amount;
        this.max = max;
        this.reload = reload;
        this.range = range;
    }

    @Override
    public String description(){
        StringBuilder des = new StringBuilder();
        des.append(abilityPro(reload/60f,"s"));
        des.append(abilityPro(range / tilesize,"格"));
        des.append(abilityPro(amount,"量"));
        des.append(abilityPro(max,"最大"));
        return des.deleteCharAt(des.length() - 1).toString();
    }

    @Override
    public void update(Unit unit){
        timer += Time.delta;

        if(timer >= reload){
            applied = false;

            Units.nearby(unit.team, unit.x, unit.y, range, other -> {
                if(other.shield < max){
                    other.shield = Math.min(other.shield + amount, max);
                    other.shieldAlpha = 1f; //TODO may not be necessary
                    applyEffect.at(unit.x, unit.y, 0f, unit.team.color, parentizeEffects ? other : null);
                    applied = true;
                }
            });

            if(applied){
                activeEffect.at(unit.x, unit.y, unit.team.color);
            }

            timer = 0f;
        }
    }
}
