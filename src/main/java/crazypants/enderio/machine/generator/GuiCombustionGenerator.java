package crazypants.enderio.machine.generator;

import java.awt.Color;
import java.awt.Rectangle;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;

import crazypants.enderio.fluid.Fluids;
import crazypants.enderio.machine.GuiMachineBase;
import crazypants.enderio.machine.power.PowerDisplayUtil;
import crazypants.gui.GuiToolTip;
import crazypants.render.ColorUtil;
import crazypants.render.RenderUtil;
import crazypants.util.Lang;

public class GuiCombustionGenerator extends GuiMachineBase {

  private TileCombustionGenerator gen;

  public GuiCombustionGenerator(InventoryPlayer par1InventoryPlayer, TileCombustionGenerator te) {
    super(te, new EmptyContainer());
    this.gen = te;
    
    addToolTip(new GuiToolTip(new Rectangle(48, 21, 15, 47), "") {

      @Override
      protected void updateText() {
        text.clear();
        String heading = Lang.localize("combustionGenerator.coolantTank");
        if(gen.coolantTank.getFluid() != null) {
          heading += ": " + gen.coolantTank.getFluid().getFluid().getLocalizedName();
        }
        text.add(heading);
        text.add(Fluids.toCapactityString(gen.coolantTank));
      }

    });

    addToolTip(new GuiToolTip(new Rectangle(114, 21, 15, 47), "") {

      @Override
      protected void updateText() {
        text.clear();
        String heading = Lang.localize("combustionGenerator.fuelTank");
        if(gen.fuelTank.getFluid() != null) {
          heading += ": " + gen.fuelTank.getFluid().getFluid().getLocalizedName();
        }
        text.add(heading);
        text.add(Fluids.toCapactityString(gen.fuelTank));
      }

    });

  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderUtil.bindTexture("enderio:textures/gui/combustionGen.png");
    int sx = (width - xSize) / 2;
    int sy = (height - ySize) / 2;
    drawTexturedModalRect(sx, sy, 0, 0, xSize, ySize);
    int scaled;

    FontRenderer fr = getFontRenderer();
    double output = 0;
    if(gen.isActive()) {
      output = gen.getCurrentOutputMj();
    }
    String txt =  Lang.localize("combustionGenerator.output") + " " + PowerDisplayUtil.formatPower(output) + " " + PowerDisplayUtil.abrevation() + PowerDisplayUtil.perTickStr();
    int sw = fr.getStringWidth(txt);
    fr.drawStringWithShadow(txt, guiLeft + xSize / 2 - sw / 2, guiTop + fr.FONT_HEIGHT / 2 + 3, ColorUtil.getRGB(Color.WHITE));

    int x = guiLeft + 48;
    int y = guiTop + 21;
    if(gen.coolantTank.getFluidAmount() > 0) {
      //left tank
      RenderUtil.renderGuiTank(gen.coolantTank.getFluid(), 4000, gen.coolantTank.getFluidAmount() - 1000, x, y, zLevel, 15, 47);

      if(gen.isActive()) {
        txt = gen.getNumTicksPerMbCoolant() + " t/MB";
        sw = fr.getStringWidth(txt);
        fr.drawStringWithShadow(txt, x - sw / 2 + 7, y + fr.FONT_HEIGHT / 2 + 47, ColorUtil.getRGB(Color.WHITE));
      }

      x = guiLeft + 72;
      y = guiTop + 14;
      //center coolant chamber
      RenderUtil.renderGuiTank(gen.coolantTank.getFluid(), 1000, Math.min(gen.coolantTank.getFluidAmount(), 1000), x, y, zLevel, 33, 33);
      //draw some gui over the top again to make the center shape
      RenderUtil.bindTexture("enderio:textures/gui/combustionGen.png");
      drawTexturedModalRect(x, y + 14, 0, 223, 33, 33);

    }

    if(gen.fuelTank.getFluidAmount() > 0) {
      //right tank
      x = guiLeft + 114;
      y += 7;
      RenderUtil.renderGuiTank(gen.fuelTank.getFluid(), 4000, gen.fuelTank.getFluidAmount() - 1000, x, y, zLevel, 15, 47);
      
      if(gen.isActive()) {
        txt = gen.getNumTicksPerMbFuel() + " t/MB";
        sw = fr.getStringWidth(txt);
        fr.drawStringWithShadow(txt, x - sw / 2 + 7, y + fr.FONT_HEIGHT / 2 + 47, ColorUtil.getRGB(Color.WHITE));
      }

      //center tank
      RenderUtil.renderGuiTank(gen.fuelTank.getFluid(), 1000, Math.min(gen.fuelTank.getFluidAmount(), 1000), guiLeft + 81, guiTop + 5, zLevel, 14, 14);
    }

    RenderUtil.bindTexture("enderio:textures/gui/combustionGen.png");
    super.drawGuiContainerBackgroundLayer(par1, par2, par3);
  }

  @Override
  protected int getPowerX() {
    return 10;
  }

  @Override
  protected int getPowerY() {
    return 13;
  }

  @Override
  protected int getPowerHeight() {
    return 60;
  }

  public static class EmptyContainer extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
      return true;
    }

  }

}