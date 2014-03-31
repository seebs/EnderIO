package crazypants.enderio.machine.generator;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import crazypants.enderio.GuiHandler;
import crazypants.enderio.ModObject;
import crazypants.enderio.machine.AbstractMachineBlock;
import crazypants.util.Util;

public class BlockCombustionGenerator extends AbstractMachineBlock<TileCombustionGenerator> {

  public static int renderId;

  protected IIcon frontOn;
  protected IIcon frontOff;

  public static BlockCombustionGenerator create() {
    BlockCombustionGenerator gen = new BlockCombustionGenerator();
    gen.init();
    return gen;
  }

  protected BlockCombustionGenerator() {
    super(ModObject.blockCombustionGenerator, TileCombustionGenerator.class);
  }

  @Override
  public void registerBlockIcons(IIconRegister iIconRegister) {
    super.registerBlockIcons(iIconRegister);
    frontOn = iIconRegister.registerIcon("enderio:combustionGenFrontOn");
    frontOff = iIconRegister.registerIcon("enderio:combustionGenFront");
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {

    TileEntity te = world.getTileEntity(x, y, z);
    if(!(te instanceof TileCombustionGenerator)) {
      return super.onBlockActivated(world, x, y, z, entityPlayer, par6, par7, par8, par9);
    }

    TileCombustionGenerator gen = (TileCombustionGenerator) te;
    ItemStack item = entityPlayer.inventory.getCurrentItem();
    if(item == null) {
      return super.onBlockActivated(world, x, y, z, entityPlayer, par6, par7, par8, par9);
    }

    //check for filled fluid containers and see if we can empty them into our tanks
    FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(item);
    if(fluid == null) {
      if(item.getItem() == Items.water_bucket) {
        fluid = new FluidStack(FluidRegistry.WATER, 1000);
      } else if(item.getItem() == Items.lava_bucket) {
        fluid = new FluidStack(FluidRegistry.LAVA, 1000);
      }
    }

    if(fluid != null) {
      int filled = gen.fill(ForgeDirection.UP, fluid, false);
      if(filled >= fluid.amount) {
        gen.fill(ForgeDirection.UP, fluid, true);
        if(!entityPlayer.capabilities.isCreativeMode) {
          entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, Util.consumeItem(item));
        }
        return true;
      }
    }

    return super.onBlockActivated(world, x, y, z, entityPlayer, par6, par7, par8, par9);
  }

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return new GuiCombustionGenerator(player.inventory, (TileCombustionGenerator) world.getTileEntity(x, y, z));
  }

  @Override
  protected int getGuiId() {
    return GuiHandler.GUI_ID_COMBUSTION_GEN;
  }

  @Override
  public int getRenderType() {
    return renderId;
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  public IIcon getBlankSideIcon() {
    return iconBuffer[0][3];
  }

  public IIcon getFrontOn() {
    return frontOn;
  }

  public IIcon getFrontOff() {
    return frontOff;
  }

  @Override
  public String getTopIconKey(boolean active) {
    return super.getTopIconKey(active);
  }

  @Override
  public String getBackIconKey(boolean active) {
    return getMachineFrontIconKey(active);
  }

  @Override
  public String getMachineFrontIconKey(boolean active) {
    return "enderio:blankMachinePanel";

  }

}