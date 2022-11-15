package mindustry.entities.abilities;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;

import static mindustry.Vars.tilesize;
import static mindustry.arcModule.RFuncs.abilityPro;

public class StatusFieldAbility extends Ability{
    public StatusEffect effect;
    public float duration = 60, reload = 100, range = 20;
    public boolean onShoot = false;
    public Effect applyEffect = Fx.none;
    public Effect activeEffect = Fx.overdriveWave;
    public float effectX, effectY;
    public boolean parentizeEffects, effectSizeParam = true;

    protected float timer;

    StatusFieldAbility(){}

    public StatusFieldAbility(StatusEffect effect, float duration, float reload, float range){
        this.duration = duration;
        this.reload = reload;
        this.range = range;
        this.effect = effect;
    }

    @Override
    public String description(){
        StringBuilder des = new StringBuilder();
        des.append(abilityPro(reload/60f,"s"));
        des.append(abilityPro(range / tilesize,"格"));
        des.append(abilityPro(duration / 60f,"s[white]" + effect.localizedName + effect.emoji()));
        return des.deleteCharAt(des.length() - 1).toString();
    }

    @Override
    public void update(Unit unit){
        timer += Time.delta;

        if(timer >= reload && (!onShoot || unit.isShooting)){
            Units.nearby(unit.team, unit.x, unit.y, range, other -> {
                other.apply(effect, duration);
                applyEffect.at(other, parentizeEffects);
            });

            float x = unit.x + Angles.trnsx(unit.rotation, effectY, effectX), y = unit.y + Angles.trnsy(unit.rotation, effectY, effectX);
            activeEffect.at(x, y, effectSizeParam ? range : unit.rotation, parentizeEffects ? unit : null);

            timer = 0f;
        }
    }
}
