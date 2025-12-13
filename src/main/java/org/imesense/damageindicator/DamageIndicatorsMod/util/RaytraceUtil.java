package org.imesense.damageindicator.DamageIndicatorsMod.util;

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
        return getClosestSolidWall(viewEntity, new Vec3d(viewEntity.posX, viewEntity.posY + 1.5d, viewEntity.posZ), traceDistance, 0, 0.0d);
    }

    public static double getClosestSolidWall(EntityLivingBase viewEntity, Vec3d startPosition, double traceDistance, int count, double offset) {
        IBlockState bs;
        Block block;
        int count2 = count + 1;
        if (count > 20 || traceDistance - offset <= 0.0d) {
            return traceDistance;
        }
        Vec3d vec31 = viewEntity.getLookVec();
        Vec3d vec32 = new Vec3d(startPosition.x + vec31.x * (traceDistance - offset), startPosition.y + vec31.y * (traceDistance - offset), startPosition.z + vec31.z * (traceDistance - offset));
        RayTraceResult objectMouseOver = viewEntity.world.rayTraceBlocks(startPosition, vec32, false, false, true);
        if (objectMouseOver != null && (block = (bs = viewEntity.world.getBlockState(objectMouseOver.getBlockPos())).getBlock()) != null) {
            if (block.getClass().getName().contains("BlockFrame")) {
                return objectMouseOver.hitVec.distanceTo(new Vec3d(viewEntity.posX, viewEntity.posY + 1.5d, viewEntity.posZ));
            }
            if (!bs.isOpaqueCube() || block.isAir(bs, viewEntity.world, objectMouseOver.getBlockPos())) {
                return getClosestSolidWall(viewEntity, objectMouseOver.hitVec.add(vec31.x, vec31.y, vec31.z), traceDistance, count2, objectMouseOver.hitVec.distanceTo(startPosition));
            }
            return objectMouseOver.hitVec.distanceTo(new Vec3d(viewEntity.posX, viewEntity.posY + 1.5d, viewEntity.posZ));
        }
        return traceDistance;
    }

    public static RayTraceResult rayTrace(EntityLivingBase viewEntity, double p_70614_1_) {
        Vec3d vec3 = new Vec3d(viewEntity.posX, viewEntity.posY + 1.5d, viewEntity.posZ);
        Vec3d vec31 = viewEntity.getLookVec();
        Vec3d vec32 = new Vec3d(vec3.x + vec31.x * p_70614_1_, vec3.y + vec31.y * p_70614_1_, vec3.z + vec31.z * p_70614_1_);
        return viewEntity.world.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public static boolean isLookingAt(EntityLivingBase viewEntity, double parDistance, float tick, Entity entity) {
        double parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance);
        if (viewEntity != null) {
            World worldObj = viewEntity.world;
            RayTraceResult objectMouseOver = rayTrace(viewEntity, parDistance2);
            if (objectMouseOver != null) {
                parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance2);
            }
            Vec3d dirVec = viewEntity.getLookVec();
            List<Entity> targettedEntities = worldObj.getEntitiesWithinAABB(Entity.class, viewEntity.getEntityBoundingBox().expand(dirVec.x * parDistance2, dirVec.y * parDistance2, dirVec.z * parDistance2));
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
                World worldObj = viewEntity.world;
                RayTraceResult objectMouseOver = rayTrace(viewEntity, parDistance2);
                Vec3d playerPosition = new Vec3d(viewEntity.posX, viewEntity.posY + 1.5d, viewEntity.posZ);
                if (objectMouseOver != null) {
                    parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance2);
                }
                Vec3d dirVec = viewEntity.getLookVec();
                Vec3d lookFarCoord = new Vec3d(playerPosition.x + dirVec.x * parDistance2, playerPosition.y + dirVec.y * parDistance2, playerPosition.z + dirVec.z * parDistance2);
                List<Entity> targettedEntities = worldObj.getEntitiesWithinAABBExcludingEntity(viewEntity, viewEntity.getEntityBoundingBox().expand(dirVec.x * parDistance2, dirVec.y * parDistance2, dirVec.z * parDistance2));
                for (Entity targettedEntity : targettedEntities) {
                    if (targettedEntity != null && !targettedEntity.isInvisible()) {
                        double precheck = viewEntity.getDistance(targettedEntity);
                        RayTraceResult mopElIntercept = targettedEntity.getEntityBoundingBox().calculateIntercept(playerPosition, lookFarCoord);
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
                World worldObj = viewEntity.world;
                RayTraceResult objectMouseOver = rayTrace(viewEntity, parDistance2);
                Vec3d playerPosition = new Vec3d(viewEntity.posX, viewEntity.posY + 1.5d, viewEntity.posZ);
                if (objectMouseOver != null) {
                    parDistance2 = getDistanceToClosestSolidWall(viewEntity, parDistance2);
                }
                Vec3d dirVec = viewEntity.getLookVec();
                Vec3d lookFarCoord = new Vec3d(playerPosition.x + dirVec.x * parDistance2, playerPosition.y + dirVec.y * parDistance2, playerPosition.z + dirVec.z * parDistance2);
                List<EntityLivingBase> targettedEntities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, viewEntity.getEntityBoundingBox().expand(dirVec.x * parDistance2, dirVec.y * parDistance2, dirVec.z * parDistance2));
                targettedEntities.remove(viewEntity);
                for (EntityLivingBase targettedEntity : targettedEntities) {
                    if (targettedEntity != null && !targettedEntity.isInvisible()) {
                        double precheck = viewEntity.getDistance(targettedEntity);
                        RayTraceResult mopElIntercept = targettedEntity.getEntityBoundingBox().calculateIntercept(playerPosition, lookFarCoord);
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
