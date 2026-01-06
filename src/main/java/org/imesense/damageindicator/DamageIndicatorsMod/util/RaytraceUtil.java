package DamageIndicatorsMod.util;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
/* loaded from: input.jar:DamageIndicatorsMod/util/RaytraceUtil.class */
public class RaytraceUtil {
    public static double getDistanceToClosestSolidWall(EntityLivingBase viewEntity, double traceDistance) {
        return getClosestSolidWall(viewEntity, new Vec3d(viewEntity.field_70165_t, viewEntity.field_70163_u + 1.5d, viewEntity.field_70161_v), traceDistance, 0, 0.0d);
    }

    public static double getClosestSolidWall(EntityLivingBase viewEntity, Vec3d startPosition, double traceDistance, int count, double offset) {
        IBlockState bs;
        Block block;
        int count2 = count + 1;
        if (count > 20 || traceDistance - offset <= 0.0d) {
            return traceDistance;
        }
        Vec3d vec31 = viewEntity.func_70040_Z();
        Vec3d vec32 = startPosition.func_72441_c(vec31.field_72450_a * (traceDistance - offset), vec31.field_72448_b * (traceDistance - offset), vec31.field_72449_c * (traceDistance - offset));
        RayTraceResult objectMouseOver = viewEntity.field_70170_p.func_147447_a(startPosition, vec32, false, false, true);
        if (objectMouseOver != null && (block = (bs = viewEntity.field_70170_p.func_180495_p(objectMouseOver.func_178782_a())).func_177230_c()) != null) {
            if (block.getClass().getName().contains("BlockFrame")) {
                return objectMouseOver.field_72307_f.func_72438_d(new Vec3d(viewEntity.field_70165_t, viewEntity.field_70163_u + 1.5d, viewEntity.field_70161_v));
            }
            if (!bs.func_185914_p() || block.isAir(bs, viewEntity.field_70170_p, objectMouseOver.func_178782_a())) {
                return getClosestSolidWall(viewEntity, objectMouseOver.field_72307_f.func_72441_c(vec31.field_72450_a, vec31.field_72448_b, vec31.field_72449_c), traceDistance, count2, objectMouseOver.field_72307_f.func_72438_d(startPosition));
            }
            return objectMouseOver.field_72307_f.func_72438_d(new Vec3d(viewEntity.field_70165_t, viewEntity.field_70163_u + 1.5d, viewEntity.field_70161_v));
        }
        return traceDistance;
    }

    public static RayTraceResult rayTrace(EntityLivingBase viewEntity, double p_70614_1_) {
        Vec3d vec3 = new Vec3d(viewEntity.field_70165_t, viewEntity.field_70163_u + 1.5d, viewEntity.field_70161_v);
        Vec3d vec31 = viewEntity.func_70040_Z();
        Vec3d vec32 = vec3.func_72441_c(vec31.field_72450_a * p_70614_1_, vec31.field_72448_b * p_70614_1_, vec31.field_72449_c * p_70614_1_);
        return viewEntity.field_70170_p.func_147447_a(vec3, vec32, false, false, true);
    }

    public static boolean isLookingAt(EntityLivingBase viewEntity, double parDistance, float tick, Entity entity) {
        double parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance);
        if (viewEntity != null) {
            World worldObj = viewEntity.field_70170_p;
            RayTraceResult objectMouseOver = rayTrace(viewEntity, parDistance2);
            if (objectMouseOver != null) {
                parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance2);
            }
            Vec3d dirVec = viewEntity.func_70040_Z();
            List<Entity> targettedEntities = worldObj.func_72872_a(Entity.class, viewEntity.func_174813_aQ().func_72321_a(dirVec.field_72450_a * parDistance2, dirVec.field_72448_b * parDistance2, dirVec.field_72449_c * parDistance2));
            return targettedEntities.contains(entity);
        }
        return false;
    }

    public static Entity getClosestEntity(EntityLivingBase viewEntity, double parDistance) {
        try {
            double parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance);
            Entity Return = null;
            double closest = parDistance2;
            if (viewEntity != null) {
                World worldObj = viewEntity.field_70170_p;
                RayTraceResult objectMouseOver = rayTrace(viewEntity, parDistance2);
                Vec3d playerPosition = new Vec3d(viewEntity.field_70165_t, viewEntity.field_70163_u + 1.5d, viewEntity.field_70161_v);
                if (objectMouseOver != null) {
                    parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance2);
                }
                Vec3d dirVec = viewEntity.func_70040_Z();
                Vec3d lookFarCoord = playerPosition.func_72441_c(dirVec.field_72450_a * parDistance2, dirVec.field_72448_b * parDistance2, dirVec.field_72449_c * parDistance2);
                List<Entity> targettedEntities = worldObj.func_72839_b(viewEntity, viewEntity.func_174813_aQ().func_72321_a(dirVec.field_72450_a * parDistance2, dirVec.field_72448_b * parDistance2, dirVec.field_72449_c * parDistance2));
                for (Entity targettedEntity : targettedEntities) {
                    if (targettedEntity != null && !targettedEntity.func_82150_aj()) {
                        double precheck = viewEntity.func_70032_d(targettedEntity);
                        RayTraceResult mopElIntercept = targettedEntity.func_174813_aQ().func_72327_a(playerPosition, lookFarCoord);
                        if (mopElIntercept != null && precheck < closest) {
                            Return = targettedEntity;
                            closest = precheck;
                        }
                    }
                }
            }
            return Return;
        } catch (Throwable th) {
            return null;
        }
    }

    public static EntityLivingBase getClosestLivingEntity(EntityLivingBase viewEntity, double parDistance) {
        try {
            double parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance);
            EntityLivingBase Return = null;
            double closest = parDistance2;
            if (viewEntity != null) {
                World worldObj = viewEntity.field_70170_p;
                RayTraceResult objectMouseOver = rayTrace(viewEntity, parDistance2);
                Vec3d playerPosition = new Vec3d(viewEntity.field_70165_t, viewEntity.field_70163_u + 1.5d, viewEntity.field_70161_v);
                if (objectMouseOver != null) {
                    parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance2);
                }
                Vec3d dirVec = viewEntity.func_70040_Z();
                Vec3d lookFarCoord = playerPosition.func_72441_c(dirVec.field_72450_a * parDistance2, dirVec.field_72448_b * parDistance2, dirVec.field_72449_c * parDistance2);
                List<EntityLivingBase> targettedEntities = worldObj.func_72872_a(EntityLivingBase.class, viewEntity.func_174813_aQ().func_72321_a(dirVec.field_72450_a * parDistance2, dirVec.field_72448_b * parDistance2, dirVec.field_72449_c * parDistance2));
                targettedEntities.remove(viewEntity);
                for (EntityLivingBase targettedEntity : targettedEntities) {
                    if (targettedEntity != null && !targettedEntity.func_82150_aj()) {
                        double precheck = viewEntity.func_70032_d(targettedEntity);
                        RayTraceResult mopElIntercept = targettedEntity.func_174813_aQ().func_72327_a(playerPosition, lookFarCoord);
                        if (mopElIntercept != null && precheck < closest) {
                            Return = targettedEntity;
                            closest = precheck;
                        }
                    }
                }
            }
            return Return;
        } catch (Throwable th) {
            return null;
        }
    }
}
