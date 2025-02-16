package com.triengine;

import com.triengine.projectors.Projector;
import com.triengine.projectors.SimpleProjector;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * wrapper hihi
 */
public class CGraphics extends Graphics2D{
    private final Graphics2D wrapped;
    private final Projector projector;

    //needed?
    final Vec e1 = Vec.o().x(100);//1
    final Vec e2 = Vec.o().y(100);
    final Vec e3 = Vec.o().z(100);
    final Vec oo = Vec.o();

    public CGraphics(Graphics2D wrapped, Projector pProjector) {
        this.wrapped = wrapped;
        this.projector = pProjector;
    }

    public void drawVector(Vec pOA){
        int[] a = onScreen(pOA);
        int[] o = onScreen(oo);
        if(a==null||o==null)return;

        drawLine(o[0], o[1], a[0], a[1]);
    }
    public void drawVector(Vec pOA, Vec pOff){
        //int[] off = onScreen(Vec.add(oo,pOff));
        int[] off = onScreen(pOff);
        int[] a = onScreen(Vec.add(pOA,pOff));
        if(off==null||a==null)return;

        drawLine(off[0], off[1], a[0], a[1]);
    }
    public void drawPoint(Vec pPt){
        int[] p = onScreen(pPt);
        if(p==null)return;

        drawOval(p[0]-5,p[1]-5,10,10);
    }


    public void drawPlane(Vec e1, Vec e2, Vec off) {
        //todo scale
        Vec off1 = Vec.add(off,e2.inv().scale(5));
        for (int i = 0; i < 10; i++) {
            drawStraight(e1,off1);
            off1 = Vec.add(off1,e2);
        }

        Vec off2 = Vec.add(off,e1.inv().scale(5));
        for (int i = 0; i < 10; i++) {
            drawStraight(e2,off2);
            off2 = Vec.add(off2,e1);
        }
    }
    public void fillPlane(Vec e1, Vec e2, Vec off) {
        //todo scale
        int[] p1 = onScreen(off);
        int[] p2 = onScreen(Vec.add(off, e1));
        int[] p3 = onScreen(Vec.add(off, Vec.add(e1, e2)));
        int[] p4 = onScreen(Vec.add(off, e2));

        int[] xPoints = {p1[0], p2[0], p3[0], p4[0]};
        int[] yPoints = {p1[1], p2[1], p3[1], p4[1]};

        wrapped.fillPolygon(xPoints, yPoints, 4);
    }

    public void drawStraight(Vec pDir){
        drawStraight(pDir, Vec.o());
    }
    public void drawStraight(Vec pDir, Vec off){
        //todo change
        Vec oa = Vec.add(off,pDir.scale(10).inv());
        Vec ob = Vec.add(off,pDir.scale(10));
        Vec ab = Vec.add(ob,oa.inv());

        drawPoint(oa);
        drawPoint(ob);

        drawVector(ab,oa);
    }

    private int[] onScreen(Vec pSysPos){
        int[] ret = projector.project(pSysPos);
        if(ret==null)return null;
        ret[0] += 500;
        ret[1] += 250;

        return ret;
    }

    /////////////////////////////////////////////////DELEGATE METHODS

    /**
     * Draws a 3-D highlighted outline of the specified rectangle.
     * The edges of the rectangle are highlighted so that they
     * appear to be beveled and lit from the upper left corner.
     * <p>
     * The colors used for the highlighting effect are determined
     * based on the current color.
     * The resulting rectangle covers an area that is
     * <code>width&nbsp;+&nbsp;1</code> pixels wide
     * by <code>height&nbsp;+&nbsp;1</code> pixels tall.  This method
     * uses the current {@code Color} exclusively and ignores
     * the current {@code Paint}.
     *
     * @param x      the x coordinate of the rectangle to be drawn.
     * @param y      the y coordinate of the rectangle to be drawn.
     * @param width  the width of the rectangle to be drawn.
     * @param height the height of the rectangle to be drawn.
     * @param raised a boolean that determines whether the rectangle
     *               appears to be raised above the surface
     *               or sunk into the surface.
     * @see Graphics#fill3DRect
     */
    @Override
    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        wrapped.draw3DRect(x, y, width, height, raised);
    }

    /**
     * Paints a 3-D highlighted rectangle filled with the current color.
     * The edges of the rectangle are highlighted so that it appears
     * as if the edges were beveled and lit from the upper left corner.
     * The colors used for the highlighting effect and for filling are
     * determined from the current {@code Color}.  This method uses
     * the current {@code Color} exclusively and ignores the current
     * {@code Paint}.
     *
     * @param x      the x coordinate of the rectangle to be filled.
     * @param y      the y coordinate of the rectangle to be filled.
     * @param width  the width of the rectangle to be filled.
     * @param height the height of the rectangle to be filled.
     * @param raised a boolean value that determines whether the
     *               rectangle appears to be raised above the surface
     *               or etched into the surface.
     * @see Graphics#draw3DRect
     */
    @Override
    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        wrapped.fill3DRect(x, y, width, height, raised);
    }

    /**
     * Strokes the outline of a {@code Shape} using the settings of the
     * current {@code Graphics2D} context.  The rendering attributes
     * applied include the {@code Clip}, {@code Transform},
     * {@code Paint}, {@code Composite} and
     * {@code Stroke} attributes.
     *
     * @param s the {@code Shape} to be rendered
     * @see #setStroke
     * @see #setPaint
     * @see Graphics#setColor
     * @see #transform
     * @see #setTransform
     * @see #clip
     * @see #setClip
     * @see #setComposite
     */
    @Override
    public void draw(Shape s) {
        wrapped.draw(s);
    }

    /**
     * Renders an image, applying a transform from image space into user space
     * before drawing.
     * The transformation from user space into device space is done with
     * the current {@code Transform} in the {@code Graphics2D}.
     * The specified transformation is applied to the image before the
     * transform attribute in the {@code Graphics2D} context is applied.
     * The rendering attributes applied include the {@code Clip},
     * {@code Transform}, and {@code Composite} attributes.
     * Note that no rendering is done if the specified transform is
     * noninvertible.
     *
     * @param img   the specified image to be rendered.
     *              This method does nothing if {@code img} is null.
     * @param xform the transformation from image space into user space
     * @param obs   the {@link ImageObserver}
     *              to be notified as more of the {@code Image}
     *              is converted
     * @return {@code true} if the {@code Image} is
     * fully loaded and completely rendered, or if it's null;
     * {@code false} if the {@code Image} is still being loaded.
     * @see #transform
     * @see #setTransform
     * @see #setComposite
     * @see #clip
     * @see #setClip
     */
    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return wrapped.drawImage(img, xform, obs);
    }

    /**
     * Renders a {@code BufferedImage} that is
     * filtered with a
     * {@link BufferedImageOp}.
     * The rendering attributes applied include the {@code Clip},
     * {@code Transform}
     * and {@code Composite} attributes.  This is equivalent to:
     * <pre>
     * img1 = op.filter(img, null);
     * drawImage(img1, new AffineTransform(1f,0f,0f,1f,x,y), null);
     * </pre>
     *
     * @param img the specified {@code BufferedImage} to be rendered.
     *            This method does nothing if {@code img} is null.
     * @param op  the filter to be applied to the image before rendering
     * @param x   the x coordinate of the location in user space where
     *            the upper left corner of the image is rendered
     * @param y   the y coordinate of the location in user space where
     *            the upper left corner of the image is rendered
     * @see #transform
     * @see #setTransform
     * @see #setComposite
     * @see #clip
     * @see #setClip
     */
    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        wrapped.drawImage(img, op, x, y);
    }

    /**
     * Renders a {@link RenderedImage},
     * applying a transform from image
     * space into user space before drawing.
     * The transformation from user space into device space is done with
     * the current {@code Transform} in the {@code Graphics2D}.
     * The specified transformation is applied to the image before the
     * transform attribute in the {@code Graphics2D} context is applied.
     * The rendering attributes applied include the {@code Clip},
     * {@code Transform}, and {@code Composite} attributes. Note
     * that no rendering is done if the specified transform is
     * noninvertible.
     *
     * @param img   the image to be rendered. This method does
     *              nothing if {@code img} is null.
     * @param xForm the transformation from image space into user space
     * @see #transform
     * @see #setTransform
     * @see #setComposite
     * @see #clip
     * @see #setClip
     */
    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xForm) {
        wrapped.drawRenderedImage(img, xForm);
    }

    /**
     * Renders a
     * {@link RenderableImage},
     * applying a transform from image space into user space before drawing.
     * The transformation from user space into device space is done with
     * the current {@code Transform} in the {@code Graphics2D}.
     * The specified transformation is applied to the image before the
     * transform attribute in the {@code Graphics2D} context is applied.
     * The rendering attributes applied include the {@code Clip},
     * {@code Transform}, and {@code Composite} attributes. Note
     * that no rendering is done if the specified transform is
     * noninvertible.
     * <p>
     * Rendering hints set on the {@code Graphics2D} object might
     * be used in rendering the {@code RenderableImage}.
     * If explicit control is required over specific hints recognized by a
     * specific {@code RenderableImage}, or if knowledge of which hints
     * are used is required, then a {@code RenderedImage} should be
     * obtained directly from the {@code RenderableImage}
     * and rendered using
     * {@link #drawRenderedImage(RenderedImage, AffineTransform) drawRenderedImage}.
     *
     * @param img   the image to be rendered. This method does
     *              nothing if {@code img} is null.
     * @param xForm the transformation from image space into user space
     * @see #transform
     * @see #setTransform
     * @see #setComposite
     * @see #clip
     * @see #setClip
     * @see #drawRenderedImage
     */
    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xForm) {
        wrapped.drawRenderableImage(img, xForm);
    }

    /**
     * Renders the text of the specified {@code String}, using the
     * current text attribute state in the {@code Graphics2D} context.
     * The baseline of the
     * first character is at position (<i>x</i>,&nbsp;<i>y</i>) in
     * the User Space.
     * The rendering attributes applied include the {@code Clip},
     * {@code Transform}, {@code Paint}, {@code Font} and
     * {@code Composite} attributes.  For characters in script
     * systems such as Hebrew and Arabic, the glyphs can be rendered from
     * right to left, in which case the coordinate supplied is the
     * location of the leftmost character on the baseline.
     *
     * @param str the string to be rendered
     * @param x   the x coordinate of the location where the
     *            {@code String} should be rendered
     * @param y   the y coordinate of the location where the
     *            {@code String} should be rendered
     * @throws NullPointerException if {@code str} is
     *                              {@code null}
     * @see Graphics#drawBytes
     * @see Graphics#drawChars
     * @since 1.0
     */
    @Override
    public void drawString(String str, int x, int y) {
        wrapped.drawString(str, x, y);
    }

    /**
     * Renders the text specified by the specified {@code String},
     * using the current text attribute state in the {@code Graphics2D} context.
     * The baseline of the first character is at position
     * (<i>x</i>,&nbsp;<i>y</i>) in the User Space.
     * The rendering attributes applied include the {@code Clip},
     * {@code Transform}, {@code Paint}, {@code Font} and
     * {@code Composite} attributes. For characters in script systems
     * such as Hebrew and Arabic, the glyphs can be rendered from right to
     * left, in which case the coordinate supplied is the location of the
     * leftmost character on the baseline.
     *
     * @param str the {@code String} to be rendered
     * @param x   the x coordinate of the location where the
     *            {@code String} should be rendered
     * @param y   the y coordinate of the location where the
     *            {@code String} should be rendered
     * @throws NullPointerException if {@code str} is
     *                              {@code null}
     * @see #setPaint
     * @see Graphics#setColor
     * @see Graphics#setFont
     * @see #setTransform
     * @see #setComposite
     * @see #setClip
     */
    @Override
    public void drawString(String str, float x, float y) {
        wrapped.drawString(str, x, y);
    }

    /**
     * Renders the text of the specified iterator applying its attributes
     * in accordance with the specification of the {@link "TextAttribute"} class.
     * <p>
     * The baseline of the first character is at position
     * (<i>x</i>,&nbsp;<i>y</i>) in User Space.
     * For characters in script systems such as Hebrew and Arabic,
     * the glyphs can be rendered from right to left, in which case the
     * coordinate supplied is the location of the leftmost character
     * on the baseline.
     *
     * @param iterator the iterator whose text is to be rendered
     * @param x        the x coordinate where the iterator's text is to be
     *                 rendered
     * @param y        the y coordinate where the iterator's text is to be
     *                 rendered
     * @throws NullPointerException if {@code iterator} is
     *                              {@code null}
     * @see #setPaint
     * @see Graphics#setColor
     * @see #setTransform
     * @see #setComposite
     * @see #setClip
     */
    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        wrapped.drawString(iterator, x, y);
    }

    /**
     * Renders the text of the specified iterator applying its attributes
     * in accordance with the specification of the {@link "TextAttribute"} class.
     * <p>
     * The baseline of the first character is at position
     * (<i>x</i>,&nbsp;<i>y</i>) in User Space.
     * For characters in script systems such as Hebrew and Arabic,
     * the glyphs can be rendered from right to left, in which case the
     * coordinate supplied is the location of the leftmost character
     * on the baseline.
     *
     * @param iterator the iterator whose text is to be rendered
     * @param x        the x coordinate where the iterator's text is to be
     *                 rendered
     * @param y        the y coordinate where the iterator's text is to be
     *                 rendered
     * @throws NullPointerException if {@code iterator} is
     *                              {@code null}
     * @see #setPaint
     * @see Graphics#setColor
     * @see #setTransform
     * @see #setComposite
     * @see #setClip
     */
    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        wrapped.drawString(iterator, x, y);
    }

    /**
     * Renders the text of the specified
     * {@link GlyphVector} using
     * the {@code Graphics2D} context's rendering attributes.
     * The rendering attributes applied include the {@code Clip},
     * {@code Transform}, {@code Paint}, and
     * {@code Composite} attributes.  The {@code GlyphVector}
     * specifies individual glyphs from a {@link Font}.
     * The {@code GlyphVector} can also contain the glyph positions.
     * This is the fastest way to render a set of characters to the
     * screen.
     *
     * @param g the {@code GlyphVector} to be rendered
     * @param x the x position in User Space where the glyphs should
     *          be rendered
     * @param y the y position in User Space where the glyphs should
     *          be rendered
     * @throws NullPointerException if {@code g} is {@code null}.
     * @see Font#createGlyphVector
     * @see GlyphVector
     * @see #setPaint
     * @see Graphics#setColor
     * @see #setTransform
     * @see #setComposite
     * @see #setClip
     */
    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        wrapped.drawGlyphVector(g, x, y);
    }

    /**
     * Fills the interior of a {@code Shape} using the settings of the
     * {@code Graphics2D} context. The rendering attributes applied
     * include the {@code Clip}, {@code Transform},
     * {@code Paint}, and {@code Composite}.
     *
     * @param s the {@code Shape} to be filled
     * @see #setPaint
     * @see Graphics#setColor
     * @see #transform
     * @see #setTransform
     * @see #setComposite
     * @see #clip
     * @see #setClip
     */
    @Override
    public void fill(Shape s) {
        wrapped.fill(s);
    }

    /**
     * Checks whether or not the specified {@code Shape} intersects
     * the specified {@link Rectangle}, which is in device
     * space. If {@code onStroke} is false, this method checks
     * whether or not the interior of the specified {@code Shape}
     * intersects the specified {@code Rectangle}.  If
     * {@code onStroke} is {@code true}, this method checks
     * whether or not the {@code Stroke} of the specified
     * {@code Shape} outline intersects the specified
     * {@code Rectangle}.
     * The rendering attributes taken into account include the
     * {@code Clip}, {@code Transform}, and {@code Stroke}
     * attributes.
     *
     * @param rect     the area in device space to check for a hit
     * @param s        the {@code Shape} to check for a hit
     * @param onStroke flag used to choose between testing the
     *                 stroked or the filled shape.  If the flag is {@code true}, the
     *                 {@code Stroke} outline is tested.  If the flag is
     *                 {@code false}, the filled {@code Shape} is tested.
     * @return {@code true} if there is a hit; {@code false}
     * otherwise.
     * @see #setStroke
     * @see #fill
     * @see #draw
     * @see #transform
     * @see #setTransform
     * @see #clip
     * @see #setClip
     */
    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return wrapped.hit(rect, s, onStroke);
    }

    /**
     * Returns the device configuration associated with this
     * {@code Graphics2D}.
     *
     * @return the device configuration of this {@code Graphics2D}.
     */
    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return wrapped.getDeviceConfiguration();
    }

    /**
     * Sets the {@code Composite} for the {@code Graphics2D} context.
     * The {@code Composite} is used in all drawing methods such as
     * {@code drawImage}, {@code drawString}, {@code draw},
     * and {@code fill}.  It specifies how new pixels are to be combined
     * with the existing pixels on the graphics device during the rendering
     * process.
     * <p>If this {@code Graphics2D} context is drawing to a
     * {@code Component} on the display screen and the
     * {@code Composite} is a custom object rather than an
     * instance of the {@code AlphaComposite} class, and if
     * there is a security manager, its {@code checkPermission}
     * method is called with an {@code AWTPermission("readDisplayPixels")}
     * permission.
     *
     * @param comp the {@code Composite} object to be used for rendering
     * @throws SecurityException if a custom {@code Composite} object is being
     *                           used to render to the screen and a security manager
     *                           is set and its {@code checkPermission} method
     *                           does not allow the operation.
     * @see Graphics#setXORMode
     * @see Graphics#setPaintMode
     * @see #getComposite
     * @see AlphaComposite
     * //@see SecurityManager #checkPermission
     * @see AWTPermission
     */
    @Override
    public void setComposite(Composite comp) {
        wrapped.setComposite(comp);
    }

    /**
     * Sets the {@code Paint} attribute for the
     * {@code Graphics2D} context.  Calling this method
     * with a {@code null Paint} object does
     * not have any effect on the current {@code Paint} attribute
     * of this {@code Graphics2D}.
     *
     * @param paint the {@code Paint} object to be used to generate
     *              color during the rendering process, or {@code null}
     * @see Graphics#setColor
     * @see #getPaint
     * @see GradientPaint
     * @see TexturePaint
     */
    @Override
    public void setPaint(Paint paint) {
        wrapped.setPaint(paint);
    }

    /**
     * Sets the {@code Stroke} for the {@code Graphics2D} context.
     *
     * @param s the {@code Stroke} object to be used to stroke a
     *          {@code Shape} during the rendering process
     * @see BasicStroke
     * @see #getStroke
     */
    @Override
    public void setStroke(Stroke s) {
        wrapped.setStroke(s);
    }

    /**
     * Sets the value of a single preference for the rendering algorithms.
     * Hint categories include controls for rendering quality and overall
     * time/quality trade-off in the rendering process.  Refer to the
     * {@code RenderingHints} class for definitions of some common
     * keys and values.
     *
     * @param hintKey   the key of the hint to be set.
     * @param hintValue the value indicating preferences for the specified
     *                  hint category.
     * @see #getRenderingHint(RenderingHints.Key)
     * @see RenderingHints
     */
    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        wrapped.setRenderingHint(hintKey, hintValue);
    }

    /**
     * Returns the value of a single preference for the rendering algorithms.
     * Hint categories include controls for rendering quality and overall
     * time/quality trade-off in the rendering process.  Refer to the
     * {@code RenderingHints} class for definitions of some common
     * keys and values.
     *
     * @param hintKey the key corresponding to the hint to get.
     * @return an object representing the value for the specified hint key.
     * Some of the keys and their associated values are defined in the
     * {@code RenderingHints} class.
     * @see RenderingHints
     * @see #setRenderingHint(RenderingHints.Key, Object)
     */
    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return wrapped.getRenderingHint(hintKey);
    }

    /**
     * Replaces the values of all preferences for the rendering
     * algorithms with the specified {@code hints}.
     * The existing values for all rendering hints are discarded and
     * the new set of known hints and values are initialized from the
     * specified {@link Map} object.
     * Hint categories include controls for rendering quality and
     * overall time/quality trade-off in the rendering process.
     * Refer to the {@code RenderingHints} class for definitions of
     * some common keys and values.
     *
     * @param hints the rendering hints to be set
     * @see #getRenderingHints
     * @see RenderingHints
     */
    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        wrapped.setRenderingHints(hints);
    }

    /**
     * Sets the values of an arbitrary number of preferences for the
     * rendering algorithms.
     * Only values for the rendering hints that are present in the
     * specified {@code Map} object are modified.
     * All other preferences not present in the specified
     * object are left unmodified.
     * Hint categories include controls for rendering quality and
     * overall time/quality trade-off in the rendering process.
     * Refer to the {@code RenderingHints} class for definitions of
     * some common keys and values.
     *
     * @param hints the rendering hints to be set
     * @see RenderingHints
     */
    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        wrapped.addRenderingHints(hints);
    }

    /**
     * Gets the preferences for the rendering algorithms.  Hint categories
     * include controls for rendering quality and overall time/quality
     * trade-off in the rendering process.
     * Returns all of the hint key/value pairs that were ever specified in
     * one operation.  Refer to the
     * {@code RenderingHints} class for definitions of some common
     * keys and values.
     *
     * @return a reference to an instance of {@code RenderingHints}
     * that contains the current preferences.
     * @see RenderingHints
     * @see #setRenderingHints(Map)
     */
    @Override
    public RenderingHints getRenderingHints() {
        return wrapped.getRenderingHints();
    }

    /**
     * Translates the origin of the {@code Graphics2D} context to the
     * point (<i>x</i>,&nbsp;<i>y</i>) in the current coordinate system.
     * Modifies the {@code Graphics2D} context so that its new origin
     * corresponds to the point (<i>x</i>,&nbsp;<i>y</i>) in the
     * {@code Graphics2D} context's former coordinate system.  All
     * coordinates used in subsequent rendering operations on this graphics
     * context are relative to this new origin.
     *
     * @param x the specified x coordinate
     * @param y the specified y coordinate
     * @since 1.0
     */
    @Override
    public void translate(int x, int y) {
        wrapped.translate(x, y);
    }

    /**
     * Concatenates the current
     * {@code Graphics2D Transform}
     * with a translation transform.
     * Subsequent rendering is translated by the specified
     * distance relative to the previous position.
     * This is equivalent to calling transform(T), where T is an
     * {@code AffineTransform} represented by the following matrix:
     * <pre>
     *          [   1    0    tx  ]
     *          [   0    1    ty  ]
     *          [   0    0    1   ]
     * </pre>
     *
     * @param tx the distance to translate along the x-axis
     * @param ty the distance to translate along the y-axis
     */
    @Override
    public void translate(double tx, double ty) {
        wrapped.translate(tx, ty);
    }

    /**
     * Concatenates the current {@code Graphics2D}
     * {@code Transform} with a rotation transform.
     * Subsequent rendering is rotated by the specified radians relative
     * to the previous origin.
     * This is equivalent to calling {@code transform(R)}, where R is an
     * {@code AffineTransform} represented by the following matrix:
     * <pre>
     *          [   cos(theta)    -sin(theta)    0   ]
     *          [   sin(theta)     cos(theta)    0   ]
     *          [       0              0         1   ]
     * </pre>
     * Rotating with a positive angle theta rotates points on the positive
     * x axis toward the positive y axis.
     *
     * @param theta the angle of rotation in radians
     */
    @Override
    public void rotate(double theta) {
        wrapped.rotate(theta);
    }

    /**
     * Concatenates the current {@code Graphics2D}
     * {@code Transform} with a translated rotation
     * transform.  Subsequent rendering is transformed by a transform
     * which is constructed by translating to the specified location,
     * rotating by the specified radians, and translating back by the same
     * amount as the original translation.  This is equivalent to the
     * following sequence of calls:
     * <pre>
     *          translate(x, y);
     *          rotate(theta);
     *          translate(-x, -y);
     * </pre>
     * Rotating with a positive angle theta rotates points on the positive
     * x axis toward the positive y axis.
     *
     * @param theta the angle of rotation in radians
     * @param x     the x coordinate of the origin of the rotation
     * @param y     the y coordinate of the origin of the rotation
     */
    @Override
    public void rotate(double theta, double x, double y) {
        wrapped.rotate(theta, x, y);
    }

    /**
     * Concatenates the current {@code Graphics2D}
     * {@code Transform} with a scaling transformation
     * Subsequent rendering is resized according to the specified scaling
     * factors relative to the previous scaling.
     * This is equivalent to calling {@code transform(S)}, where S is an
     * {@code AffineTransform} represented by the following matrix:
     * <pre>
     *          [   sx   0    0   ]
     *          [   0    sy   0   ]
     *          [   0    0    1   ]
     * </pre>
     *
     * @param sx the amount by which X coordinates in subsequent
     *           rendering operations are multiplied relative to previous
     *           rendering operations.
     * @param sy the amount by which Y coordinates in subsequent
     *           rendering operations are multiplied relative to previous
     *           rendering operations.
     */
    @Override
    public void scale(double sx, double sy) {
        wrapped.scale(sx, sy);
    }

    /**
     * Concatenates the current {@code Graphics2D}
     * {@code Transform} with a shearing transform.
     * Subsequent renderings are sheared by the specified
     * multiplier relative to the previous position.
     * This is equivalent to calling {@code transform(SH)}, where SH
     * is an {@code AffineTransform} represented by the following
     * matrix:
     * <pre>
     *          [   1   shx   0   ]
     *          [  shy   1    0   ]
     *          [   0    0    1   ]
     * </pre>
     *
     * @param shx the multiplier by which coordinates are shifted in
     *            the positive X axis direction as a function of their Y coordinate
     * @param shy the multiplier by which coordinates are shifted in
     *            the positive Y axis direction as a function of their X coordinate
     */
    @Override
    public void shear(double shx, double shy) {
        wrapped.shear(shx, shy);
    }

    /**
     * Composes an {@code AffineTransform} object with the
     * {@code Transform} in this {@code Graphics2D} according
     * to the rule last-specified-first-applied.  If the current
     * {@code Transform} is Cx, the result of composition
     * with Tx is a new {@code Transform} Cx'.  Cx' becomes the
     * current {@code Transform} for this {@code Graphics2D}.
     * Transforming a point p by the updated {@code Transform} Cx' is
     * equivalent to first transforming p by Tx and then transforming
     * the result by the original {@code Transform} Cx.  In other
     * words, Cx'(p) = Cx(Tx(p)).  A copy of the Tx is made, if necessary,
     * so further modifications to Tx do not affect rendering.
     *
     * @param Tx the {@code AffineTransform} object to be composed with
     *           the current {@code Transform}
     * @see #setTransform
     * @see AffineTransform
     */
    @Override
    public void transform(AffineTransform Tx) {
        wrapped.transform(Tx);
    }

    /**
     * Overwrites the Transform in the {@code Graphics2D} context.
     * WARNING: This method should <b>never</b> be used to apply a new
     * coordinate transform on top of an existing transform because the
     * {@code Graphics2D} might already have a transform that is
     * needed for other purposes, such as rendering Swing
     * components or applying a scaling transformation to adjust for the
     * resolution of a printer.
     * <p>To add a coordinate transform, use the
     * {@code transform}, {@code rotate}, {@code scale},
     * or {@code shear} methods.  The {@code setTransform}
     * method is intended only for restoring the original
     * {@code Graphics2D} transform after rendering, as shown in this
     * example:
     * <pre>
     * // Get the current transform
     * AffineTransform saveAT = g2.getTransform();
     * // Perform transformation
     * g2d.transform(...);
     * // Render
     * g2d.draw(...);
     * // Restore original transform
     * g2d.setTransform(saveAT);
     * </pre>
     *
     * @param Tx the {@code AffineTransform} that was retrieved
     *           from the {@code getTransform} method
     * @see #transform
     * @see #getTransform
     * @see AffineTransform
     */
    @Override
    public void setTransform(AffineTransform Tx) {
        wrapped.setTransform(Tx);
    }

    /**
     * Returns a copy of the current {@code Transform} in the
     * {@code Graphics2D} context.
     *
     * @return the current {@code AffineTransform} in the
     * {@code Graphics2D} context.
     * @see #transform
     * @see #setTransform
     */
    @Override
    public AffineTransform getTransform() {
        return wrapped.getTransform();
    }

    /**
     * Returns the current {@code Paint} of the
     * {@code Graphics2D} context.
     *
     * @return the current {@code Graphics2D Paint},
     * which defines a color or pattern.
     * @see #setPaint
     * @see Graphics#setColor
     */
    @Override
    public Paint getPaint() {
        return wrapped.getPaint();
    }

    /**
     * Returns the current {@code Composite} in the
     * {@code Graphics2D} context.
     *
     * @return the current {@code Graphics2D Composite},
     * which defines a compositing style.
     * @see #setComposite
     */
    @Override
    public Composite getComposite() {
        return wrapped.getComposite();
    }

    /**
     * Sets the background color for the {@code Graphics2D} context.
     * The background color is used for clearing a region.
     * When a {@code Graphics2D} is constructed for a
     * {@code Component}, the background color is
     * inherited from the {@code Component}. Setting the background color
     * in the {@code Graphics2D} context only affects the subsequent
     * {@code clearRect} calls and not the background color of the
     * {@code Component}.  To change the background
     * of the {@code Component}, use appropriate methods of
     * the {@code Component}.
     *
     * @param color the background color that is used in
     *              subsequent calls to {@code clearRect}
     * @see #getBackground
     * @see Graphics#clearRect
     */
    @Override
    public void setBackground(Color color) {
        wrapped.setBackground(color);
    }

    /**
     * Returns the background color used for clearing a region.
     *
     * @return the current {@code Graphics2D Color},
     * which defines the background color.
     * @see #setBackground
     */
    @Override
    public Color getBackground() {
        return wrapped.getBackground();
    }

    /**
     * Returns the current {@code Stroke} in the
     * {@code Graphics2D} context.
     *
     * @return the current {@code Graphics2D Stroke},
     * which defines the line style.
     * @see #setStroke
     */
    @Override
    public Stroke getStroke() {
        return wrapped.getStroke();
    }

    /**
     * Intersects the current {@code Clip} with the interior of the
     * specified {@code Shape} and sets the {@code Clip} to the
     * resulting intersection.  The specified {@code Shape} is
     * transformed with the current {@code Graphics2D}
     * {@code Transform} before being intersected with the current
     * {@code Clip}.  This method is used to make the current
     * {@code Clip} smaller.
     * To make the {@code Clip} larger, use {@code setClip}.
     * <p>The <i>user clip</i> modified by this method is independent of the
     * clipping associated with device bounds and visibility.  If no clip has
     * previously been set, or if the clip has been cleared using
     * {@link Graphics#setClip(Shape) setClip} with a {@code null}
     * argument, the specified {@code Shape} becomes the new
     * user clip.
     * <p>Since this method intersects the specified shape
     * with the current clip, it will throw {@code NullPointerException}
     * for a {@code null} shape unless the user clip is also {@code null}.
     * So calling this method with a {@code null} argument is not recommended.
     *
     * @param s the {@code Shape} to be intersected with the current
     *          {@code Clip}. This method updates the current {@code Clip}.
     * @throws NullPointerException if {@code s} is {@code null}
     *                              and a user clip is currently set.
     */
    @Override
    public void clip(Shape s) {
        wrapped.clip(s);
    }

    /**
     * Get the rendering context of the {@code Font} within this
     * {@code Graphics2D} context.
     * The {@link FontRenderContext}
     * encapsulates application hints such as anti-aliasing and
     * fractional metrics, as well as target device specific information
     * such as dots-per-inch.  This information should be provided by the
     * application when using objects that perform typographical
     * formatting, such as {@code Font} and
     * {@code TextLayout}.  This information should also be provided
     * by applications that perform their own layout and need accurate
     * measurements of various characteristics of glyphs such as advance
     * and line height when various rendering hints have been applied to
     * the text rendering.
     *
     * @return a reference to an instance of FontRenderContext.
     * @see FontRenderContext
     * @see Font#createGlyphVector
     * @see TextLayout
     * @since 1.2
     */
    @Override
    public FontRenderContext getFontRenderContext() {
        return wrapped.getFontRenderContext();
    }

    /**
     * Creates a new {@code Graphics} object that is
     * a copy of this {@code Graphics} object.
     *
     * @return a new graphics context that is a copy of
     * this graphics context.
     */
    @Override
    public Graphics create() {
        return wrapped.create();
    }

    /**
     * Creates a new {@code Graphics} object based on this
     * {@code Graphics} object, but with a new translation and clip area.
     * The new {@code Graphics} object has its origin
     * translated to the specified point (<i>x</i>,&nbsp;<i>y</i>).
     * Its clip area is determined by the intersection of the original
     * clip area with the specified rectangle.  The arguments are all
     * interpreted in the coordinate system of the original
     * {@code Graphics} object. The new graphics context is
     * identical to the original, except in two respects:
     *
     * <ul>
     * <li>
     * The new graphics context is translated by (<i>x</i>,&nbsp;<i>y</i>).
     * That is to say, the point ({@code 0},&nbsp;{@code 0}) in the
     * new graphics context is the same as (<i>x</i>,&nbsp;<i>y</i>) in
     * the original graphics context.
     * <li>
     * The new graphics context has an additional clipping rectangle, in
     * addition to whatever (translated) clipping rectangle it inherited
     * from the original graphics context. The origin of the new clipping
     * rectangle is at ({@code 0},&nbsp;{@code 0}), and its size
     * is specified by the {@code width} and {@code height}
     * arguments.
     * </ul>
     *
     * @param x      the <i>x</i> coordinate.
     * @param y      the <i>y</i> coordinate.
     * @param width  the width of the clipping rectangle.
     * @param height the height of the clipping rectangle.
     * @return a new graphics context.
     * @see Graphics#translate
     * @see Graphics#clipRect
     */
    @Override
    public Graphics create(int x, int y, int width, int height) {
        return wrapped.create(x, y, width, height);
    }

    /**
     * Gets this graphics context's current color.
     *
     * @return this graphics context's current color.
     * @see Color
     * @see Graphics#setColor(Color)
     */
    @Override
    public Color getColor() {
        return wrapped.getColor();
    }

    /**
     * Sets this graphics context's current color to the specified
     * color. All subsequent graphics operations using this graphics
     * context use this specified color.
     * A null argument is silently ignored.
     *
     * @param c the new rendering color.
     * @see Color
     * @see Graphics#getColor
     */
    @Override
    public void setColor(Color c) {
        wrapped.setColor(c);
    }

    /**
     * Sets the paint mode of this graphics context to overwrite the
     * destination with this graphics context's current color.
     * This sets the logical pixel operation function to the paint or
     * overwrite mode.  All subsequent rendering operations will
     * overwrite the destination with the current color.
     */
    @Override
    public void setPaintMode() {
        wrapped.setPaintMode();
    }

    /**
     * Sets the paint mode of this graphics context to alternate between
     * this graphics context's current color and the new specified color.
     * This specifies that logical pixel operations are performed in the
     * XOR mode, which alternates pixels between the current color and
     * a specified XOR color.
     * <p>
     * When drawing operations are performed, pixels which are the
     * current color are changed to the specified color, and vice versa.
     * <p>
     * Pixels that are of colors other than those two colors are changed
     * in an unpredictable but reversible manner; if the same figure is
     * drawn twice, then all pixels are restored to their original values.
     *
     * @param c1 the XOR alternation color
     */
    @Override
    public void setXORMode(Color c1) {
        wrapped.setXORMode(c1);
    }

    /**
     * Gets the current font.
     *
     * @return this graphics context's current font.
     * @see Font
     * @see Graphics#setFont(Font)
     */
    @Override
    public Font getFont() {
        return wrapped.getFont();
    }

    /**
     * Sets this graphics context's font to the specified font.
     * All subsequent text operations using this graphics context
     * use this font. A null argument is silently ignored.
     *
     * @param font the font.
     * @see Graphics#getFont
     * @see Graphics#drawString(String, int, int)
     * @see Graphics#drawBytes(byte[], int, int, int, int)
     * @see Graphics#drawChars(char[], int, int, int, int)
     */
    @Override
    public void setFont(Font font) {
        wrapped.setFont(font);
    }

    /**
     * Gets the font metrics of the current font.
     *
     * @return the font metrics of this graphics
     * context's current font.
     * @see Graphics#getFont
     * @see FontMetrics
     * @see Graphics#getFontMetrics(Font)
     */
    @Override
    public FontMetrics getFontMetrics() {
        return wrapped.getFontMetrics();
    }

    /**
     * Gets the font metrics for the specified font.
     *
     * @param f the specified font
     * @return the font metrics for the specified font.
     * @see Graphics#getFont
     * @see FontMetrics
     * @see Graphics#getFontMetrics()
     */
    @Override
    public FontMetrics getFontMetrics(Font f) {
        return wrapped.getFontMetrics(f);
    }

    /**
     * Returns the bounding rectangle of the current clipping area.
     * This method refers to the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * If no clip has previously been set, or if the clip has been
     * cleared using {@code setClip(null)}, this method returns
     * {@code null}.
     * The coordinates in the rectangle are relative to the coordinate
     * system origin of this graphics context.
     *
     * @return the bounding rectangle of the current clipping area,
     * or {@code null} if no clip is set.
     * @see Graphics#getClip
     * @see Graphics#clipRect
     * @see Graphics#setClip(int, int, int, int)
     * @see Graphics#setClip(Shape)
     * @since 1.1
     */
    @Override
    public Rectangle getClipBounds() {
        return wrapped.getClipBounds();
    }

    /**
     * Intersects the current clip with the specified rectangle.
     * The resulting clipping area is the intersection of the current
     * clipping area and the specified rectangle.  If there is no
     * current clipping area, either because the clip has never been
     * set, or the clip has been cleared using {@code setClip(null)},
     * the specified rectangle becomes the new clip.
     * This method sets the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * This method can only be used to make the current clip smaller.
     * To set the current clip larger, use any of the setClip methods.
     * Rendering operations have no effect outside of the clipping area.
     *
     * @param x      the x coordinate of the rectangle to intersect the clip with
     * @param y      the y coordinate of the rectangle to intersect the clip with
     * @param width  the width of the rectangle to intersect the clip with
     * @param height the height of the rectangle to intersect the clip with
     * @see #setClip(int, int, int, int)
     * @see #setClip(Shape)
     */
    @Override
    public void clipRect(int x, int y, int width, int height) {
        wrapped.clipRect(x, y, width, height);
    }

    /**
     * Sets the current clip to the rectangle specified by the given
     * coordinates.  This method sets the user clip, which is
     * independent of the clipping associated with device bounds
     * and window visibility.
     * Rendering operations have no effect outside of the clipping area.
     *
     * @param x      the <i>x</i> coordinate of the new clip rectangle.
     * @param y      the <i>y</i> coordinate of the new clip rectangle.
     * @param width  the width of the new clip rectangle.
     * @param height the height of the new clip rectangle.
     * @see Graphics#clipRect
     * @see Graphics#setClip(Shape)
     * @see Graphics#getClip
     * @since 1.1
     */
    @Override
    public void setClip(int x, int y, int width, int height) {
        wrapped.setClip(x, y, width, height);
    }

    /**
     * Gets the current clipping area.
     * This method returns the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * If no clip has previously been set, or if the clip has been
     * cleared using {@code setClip(null)}, this method returns
     * {@code null}.
     *
     * @return a {@code Shape} object representing the
     * current clipping area, or {@code null} if
     * no clip is set.
     * @see Graphics#getClipBounds
     * @see Graphics#clipRect
     * @see Graphics#setClip(int, int, int, int)
     * @see Graphics#setClip(Shape)
     * @since 1.1
     */
    @Override
    public Shape getClip() {
        return wrapped.getClip();
    }

    /**
     * Sets the current clipping area to an arbitrary clip shape.
     * Not all objects that implement the {@code Shape}
     * interface can be used to set the clip.  The only
     * {@code Shape} objects that are guaranteed to be
     * supported are {@code Shape} objects that are
     * obtained via the {@code getClip} method and via
     * {@code Rectangle} objects.  This method sets the
     * user clip, which is independent of the clipping associated
     * with device bounds and window visibility.
     *
     * @param clip the {@code Shape} to use to set the clip.
     *             Passing {@code null} clears the current {@code clip}.
     * @see Graphics#getClip()
     * @see Graphics#clipRect
     * @see Graphics#setClip(int, int, int, int)
     * @since 1.1
     */
    @Override
    public void setClip(Shape clip) {
        wrapped.setClip(clip);
    }

    /**
     * Copies an area of the component by a distance specified by
     * {@code dx} and {@code dy}. From the point specified
     * by {@code x} and {@code y}, this method
     * copies downwards and to the right.  To copy an area of the
     * component to the left or upwards, specify a negative value for
     * {@code dx} or {@code dy}.
     * If a portion of the source rectangle lies outside the bounds
     * of the component, or is obscured by another window or component,
     * {@code copyArea} will be unable to copy the associated
     * pixels. The area that is omitted can be refreshed by calling
     * the component's {@code paint} method.
     *
     * @param x      the <i>x</i> coordinate of the source rectangle.
     * @param y      the <i>y</i> coordinate of the source rectangle.
     * @param width  the width of the source rectangle.
     * @param height the height of the source rectangle.
     * @param dx     the horizontal distance to copy the pixels.
     * @param dy     the vertical distance to copy the pixels.
     */
    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        wrapped.copyArea(x, y, width, height, dx, dy);
    }

    /**
     * Draws a line, using the current color, between the points
     * <code>(x1,&nbsp;y1)</code> and <code>(x2,&nbsp;y2)</code>
     * in this graphics context's coordinate system.
     *
     * @param x1 the first point's <i>x</i> coordinate.
     * @param y1 the first point's <i>y</i> coordinate.
     * @param x2 the second point's <i>x</i> coordinate.
     * @param y2 the second point's <i>y</i> coordinate.
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        wrapped.drawLine(x1, y1, x2, y2);
    }

    /**
     * Fills the specified rectangle.
     * The left and right edges of the rectangle are at
     * {@code x} and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>.
     * The top and bottom edges are at
     * {@code y} and <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>.
     * The resulting rectangle covers an area
     * {@code width} pixels wide by
     * {@code height} pixels tall.
     * The rectangle is filled using the graphics context's current color.
     *
     * @param x      the <i>x</i> coordinate
     *               of the rectangle to be filled.
     * @param y      the <i>y</i> coordinate
     *               of the rectangle to be filled.
     * @param width  the width of the rectangle to be filled.
     * @param height the height of the rectangle to be filled.
     * @see Graphics#clearRect
     * @see Graphics#drawRect
     */
    @Override
    public void fillRect(int x, int y, int width, int height) {
        wrapped.fillRect(x, y, width, height);
    }

    /**
     * Draws the outline of the specified rectangle.
     * The left and right edges of the rectangle are at
     * {@code x} and <code>x&nbsp;+&nbsp;width</code>.
     * The top and bottom edges are at
     * {@code y} and <code>y&nbsp;+&nbsp;height</code>.
     * The rectangle is drawn using the graphics context's current color.
     *
     * @param x      the <i>x</i> coordinate
     *               of the rectangle to be drawn.
     * @param y      the <i>y</i> coordinate
     *               of the rectangle to be drawn.
     * @param width  the width of the rectangle to be drawn.
     * @param height the height of the rectangle to be drawn.
     * @see Graphics#fillRect
     * @see Graphics#clearRect
     */
    @Override
    public void drawRect(int x, int y, int width, int height) {
        wrapped.drawRect(x, y, width, height);
    }

    /**
     * Clears the specified rectangle by filling it with the background
     * color of the current drawing surface. This operation does not
     * use the current paint mode.
     * <p>
     * Beginning with Java&nbsp;1.1, the background color
     * of offscreen images may be system dependent. Applications should
     * use {@code setColor} followed by {@code fillRect} to
     * ensure that an offscreen image is cleared to a specific color.
     *
     * @param x      the <i>x</i> coordinate of the rectangle to clear.
     * @param y      the <i>y</i> coordinate of the rectangle to clear.
     * @param width  the width of the rectangle to clear.
     * @param height the height of the rectangle to clear.
     * @see Graphics#fillRect(int, int, int, int)
     * @see Graphics#drawRect
     * @see Graphics#setColor(Color)
     * @see Graphics#setPaintMode
     * @see Graphics#setXORMode(Color)
     */
    @Override
    public void clearRect(int x, int y, int width, int height) {
        wrapped.clearRect(x, y, width, height);
    }

    /**
     * Draws an outlined round-cornered rectangle using this graphics
     * context's current color. The left and right edges of the rectangle
     * are at {@code x} and <code>x&nbsp;+&nbsp;width</code>,
     * respectively. The top and bottom edges of the rectangle are at
     * {@code y} and <code>y&nbsp;+&nbsp;height</code>.
     *
     * @param x         the <i>x</i> coordinate of the rectangle to be drawn.
     * @param y         the <i>y</i> coordinate of the rectangle to be drawn.
     * @param width     the width of the rectangle to be drawn.
     * @param height    the height of the rectangle to be drawn.
     * @param arcWidth  the horizontal diameter of the arc
     *                  at the four corners.
     * @param arcHeight the vertical diameter of the arc
     *                  at the four corners.
     * @see Graphics#fillRoundRect
     */
    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        wrapped.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * Fills the specified rounded corner rectangle with the current color.
     * The left and right edges of the rectangle
     * are at {@code x} and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>,
     * respectively. The top and bottom edges of the rectangle are at
     * {@code y} and <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>.
     *
     * @param x         the <i>x</i> coordinate of the rectangle to be filled.
     * @param y         the <i>y</i> coordinate of the rectangle to be filled.
     * @param width     the width of the rectangle to be filled.
     * @param height    the height of the rectangle to be filled.
     * @param arcWidth  the horizontal diameter
     *                  of the arc at the four corners.
     * @param arcHeight the vertical diameter
     *                  of the arc at the four corners.
     * @see Graphics#drawRoundRect
     */
    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        wrapped.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    /**
     * Draws the outline of an oval.
     * The result is a circle or ellipse that fits within the
     * rectangle specified by the {@code x}, {@code y},
     * {@code width}, and {@code height} arguments.
     * <p>
     * The oval covers an area that is
     * <code>width&nbsp;+&nbsp;1</code> pixels wide
     * and <code>height&nbsp;+&nbsp;1</code> pixels tall.
     *
     * @param x      the <i>x</i> coordinate of the upper left
     *               corner of the oval to be drawn.
     * @param y      the <i>y</i> coordinate of the upper left
     *               corner of the oval to be drawn.
     * @param width  the width of the oval to be drawn.
     * @param height the height of the oval to be drawn.
     * @see Graphics#fillOval
     */
    @Override
    public void drawOval(int x, int y, int width, int height) {
        wrapped.drawOval(x, y, width, height);
    }

    /**
     * Fills an oval bounded by the specified rectangle with the
     * current color.
     *
     * @param x      the <i>x</i> coordinate of the upper left corner
     *               of the oval to be filled.
     * @param y      the <i>y</i> coordinate of the upper left corner
     *               of the oval to be filled.
     * @param width  the width of the oval to be filled.
     * @param height the height of the oval to be filled.
     * @see Graphics#drawOval
     */
    @Override
    public void fillOval(int x, int y, int width, int height) {
        wrapped.fillOval(x, y, width, height);
    }

    /**
     * Draws the outline of a circular or elliptical arc
     * covering the specified rectangle.
     * <p>
     * The resulting arc begins at {@code startAngle} and extends
     * for {@code arcAngle} degrees, using the current color.
     * Angles are interpreted such that 0&nbsp;degrees
     * is at the 3&nbsp;o'clock position.
     * A positive value indicates a counter-clockwise rotation
     * while a negative value indicates a clockwise rotation.
     * <p>
     * The center of the arc is the center of the rectangle whose origin
     * is (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the
     * {@code width} and {@code height} arguments.
     * <p>
     * The resulting arc covers an area
     * <code>width&nbsp;+&nbsp;1</code> pixels wide
     * by <code>height&nbsp;+&nbsp;1</code> pixels tall.
     * <p>
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the
     * start and end of the arc segment will be skewed farther along the
     * longer axis of the bounds.
     *
     * @param x          the <i>x</i> coordinate of the
     *                   upper-left corner of the arc to be drawn.
     * @param y          the <i>y</i>  coordinate of the
     *                   upper-left corner of the arc to be drawn.
     * @param width      the width of the arc to be drawn.
     * @param height     the height of the arc to be drawn.
     * @param startAngle the beginning angle.
     * @param arcAngle   the angular extent of the arc,
     *                   relative to the start angle.
     * @see Graphics#fillArc
     */
    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        wrapped.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * Fills a circular or elliptical arc covering the specified rectangle.
     * <p>
     * The resulting arc begins at {@code startAngle} and extends
     * for {@code arcAngle} degrees.
     * Angles are interpreted such that 0&nbsp;degrees
     * is at the 3&nbsp;o'clock position.
     * A positive value indicates a counter-clockwise rotation
     * while a negative value indicates a clockwise rotation.
     * <p>
     * The center of the arc is the center of the rectangle whose origin
     * is (<i>x</i>,&nbsp;<i>y</i>) and whose size is specified by the
     * {@code width} and {@code height} arguments.
     * <p>
     * The resulting arc covers an area
     * <code>width&nbsp;+&nbsp;1</code> pixels wide
     * by <code>height&nbsp;+&nbsp;1</code> pixels tall.
     * <p>
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the
     * start and end of the arc segment will be skewed farther along the
     * longer axis of the bounds.
     *
     * @param x          the <i>x</i> coordinate of the
     *                   upper-left corner of the arc to be filled.
     * @param y          the <i>y</i>  coordinate of the
     *                   upper-left corner of the arc to be filled.
     * @param width      the width of the arc to be filled.
     * @param height     the height of the arc to be filled.
     * @param startAngle the beginning angle.
     * @param arcAngle   the angular extent of the arc,
     *                   relative to the start angle.
     * @see Graphics#drawArc
     */
    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        wrapped.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    /**
     * Draws a sequence of connected lines defined by
     * arrays of <i>x</i> and <i>y</i> coordinates.
     * Each pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates defines a point.
     * The figure is not closed if the first point
     * differs from the last point.
     *
     * @param xPoints an array of <i>x</i> points
     * @param yPoints an array of <i>y</i> points
     * @param nPoints the total number of points
     * @see Graphics#drawPolygon(int[], int[], int)
     * @since 1.1
     */
    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        wrapped.drawPolyline(xPoints, yPoints, nPoints);
    }

    /**
     * Draws a closed polygon defined by
     * arrays of <i>x</i> and <i>y</i> coordinates.
     * Each pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates defines a point.
     * <p>
     * This method draws the polygon defined by {@code nPoint} line
     * segments, where the first <code>nPoint&nbsp;-&nbsp;1</code>
     * line segments are line segments from
     * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code>
     * to <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
     * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;{@code nPoints}.
     * The figure is automatically closed by drawing a line connecting
     * the final point to the first point, if those points are different.
     *
     * @param xPoints an array of {@code x} coordinates.
     * @param yPoints an array of {@code y} coordinates.
     * @param nPoints the total number of points.
     * @see Graphics#fillPolygon
     * @see Graphics#drawPolyline
     */
    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        wrapped.drawPolygon(xPoints, yPoints, nPoints);
    }

    /**
     * Draws the outline of a polygon defined by the specified
     * {@code Polygon} object.
     *
     * @param p the polygon to draw.
     * @see Graphics#fillPolygon
     * @see Graphics#drawPolyline
     */
    @Override
    public void drawPolygon(Polygon p) {
        wrapped.drawPolygon(p);
    }

    /**
     * Fills a closed polygon defined by
     * arrays of <i>x</i> and <i>y</i> coordinates.
     * <p>
     * This method draws the polygon defined by {@code nPoint} line
     * segments, where the first <code>nPoint&nbsp;-&nbsp;1</code>
     * line segments are line segments from
     * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code>
     * to <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
     * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;{@code nPoints}.
     * The figure is automatically closed by drawing a line connecting
     * the final point to the first point, if those points are different.
     * <p>
     * The area inside the polygon is defined using an
     * even-odd fill rule, also known as the alternating rule.
     *
     * @param xPoints an array of {@code x} coordinates.
     * @param yPoints an array of {@code y} coordinates.
     * @param nPoints the total number of points.
     * @see Graphics#drawPolygon(int[], int[], int)
     */
    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        wrapped.fillPolygon(xPoints, yPoints, nPoints);
    }

    /**
     * Fills the polygon defined by the specified Polygon object with
     * the graphics context's current color.
     * <p>
     * The area inside the polygon is defined using an
     * even-odd fill rule, also known as the alternating rule.
     *
     * @param p the polygon to fill.
     * @see Graphics#drawPolygon(int[], int[], int)
     */
    @Override
    public void fillPolygon(Polygon p) {
        wrapped.fillPolygon(p);
    }

    /**
     * Draws the text given by the specified character array, using this
     * graphics context's current font and color. The baseline of the
     * first character is at position (<i>x</i>,&nbsp;<i>y</i>) in this
     * graphics context's coordinate system.
     *
     * @param data   the array of characters to be drawn
     * @param offset the start offset in the data
     * @param length the number of characters to be drawn
     * @param x      the <i>x</i> coordinate of the baseline of the text
     * @param y      the <i>y</i> coordinate of the baseline of the text
     * @throws NullPointerException      if {@code data} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code offset} or
     *                                   {@code length} is less than zero, or
     *                                   {@code offset+length} is greater than the length of the
     *                                   {@code data} array.
     * @see Graphics#drawBytes
     * @see Graphics#drawString
     */
    @Override
    public void drawChars(char[] data, int offset, int length, int x, int y) {
        wrapped.drawChars(data, offset, length, x, y);
    }

    /**
     * Draws the text given by the specified byte array, using this
     * graphics context's current font and color. The baseline of the
     * first character is at position (<i>x</i>,&nbsp;<i>y</i>) in this
     * graphics context's coordinate system.
     * <p>
     * Use of this method is not recommended as each byte is interpreted
     * as a Unicode code point in the range 0 to 255, and so can only be
     * used to draw Latin characters in that range.
     *
     * @param data   the data to be drawn
     * @param offset the start offset in the data
     * @param length the number of bytes that are drawn
     * @param x      the <i>x</i> coordinate of the baseline of the text
     * @param y      the <i>y</i> coordinate of the baseline of the text
     * @throws NullPointerException      if {@code data} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code offset} or
     *                                   {@code length} is less than zero, or {@code offset+length}
     *                                   is greater than the length of the {@code data} array.
     * @see Graphics#drawChars
     * @see Graphics#drawString
     */
    @Override
    public void drawBytes(byte[] data, int offset, int length, int x, int y) {
        wrapped.drawBytes(data, offset, length, x, y);
    }

    /**
     * Draws as much of the specified image as is currently available.
     * The image is drawn with its top-left corner at
     * (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's coordinate
     * space. Transparent pixels in the image do not affect whatever
     * pixels are already there.
     * <p>
     * This method returns immediately in all cases, even if the
     * complete image has not yet been loaded, and it has not been dithered
     * and converted for the current output device.
     * <p>
     * If the image has completely loaded and its pixels are
     * no longer being changed, then
     * {@code drawImage} returns {@code true}.
     * Otherwise, {@code drawImage} returns {@code false}
     * and as more of
     * the image becomes available
     * or it is time to draw another frame of animation,
     * the process that loads the image notifies
     * the specified image observer.
     *
     * @param img      the specified image to be drawn. This method does
     *                 nothing if {@code img} is null.
     * @param x        the <i>x</i> coordinate.
     * @param y        the <i>y</i> coordinate.
     * @param observer object to be notified as more of
     *                 the image is converted.
     * @return {@code false} if the image pixels are still changing;
     * {@code true} otherwise.
     * @see Image
     * @see ImageObserver
     * @see ImageObserver#imageUpdate(Image, int, int, int, int, int)
     */
    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return wrapped.drawImage(img, x, y, observer);
    }

    /**
     * Draws as much of the specified image as has already been scaled
     * to fit inside the specified rectangle.
     * <p>
     * The image is drawn inside the specified rectangle of this
     * graphics context's coordinate space, and is scaled if
     * necessary. Transparent pixels do not affect whatever pixels
     * are already there.
     * <p>
     * This method returns immediately in all cases, even if the
     * entire image has not yet been scaled, dithered, and converted
     * for the current output device.
     * If the current output representation is not yet complete, then
     * {@code drawImage} returns {@code false}. As more of
     * the image becomes available, the process that loads the image notifies
     * the image observer by calling its {@code imageUpdate} method.
     * <p>
     * A scaled version of an image will not necessarily be
     * available immediately just because an unscaled version of the
     * image has been constructed for this output device.  Each size of
     * the image may be cached separately and generated from the original
     * data in a separate image production sequence.
     *
     * @param img      the specified image to be drawn. This method does
     *                 nothing if {@code img} is null.
     * @param x        the <i>x</i> coordinate.
     * @param y        the <i>y</i> coordinate.
     * @param width    the width of the rectangle.
     * @param height   the height of the rectangle.
     * @param observer object to be notified as more of
     *                 the image is converted.
     * @return {@code false} if the image pixels are still changing;
     * {@code true} otherwise.
     * @see Image
     * @see ImageObserver
     * @see ImageObserver#imageUpdate(Image, int, int, int, int, int)
     */
    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return wrapped.drawImage(img, x, y, width, height, observer);
    }

    /**
     * Draws as much of the specified image as is currently available.
     * The image is drawn with its top-left corner at
     * (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's coordinate
     * space.  Transparent pixels are drawn in the specified
     * background color.
     * <p>
     * This operation is equivalent to filling a rectangle of the
     * width and height of the specified image with the given color and then
     * drawing the image on top of it, but possibly more efficient.
     * <p>
     * This method returns immediately in all cases, even if the
     * complete image has not yet been loaded, and it has not been dithered
     * and converted for the current output device.
     * <p>
     * If the image has completely loaded and its pixels are
     * no longer being changed, then
     * {@code drawImage} returns {@code true}.
     * Otherwise, {@code drawImage} returns {@code false}
     * and as more of
     * the image becomes available
     * or it is time to draw another frame of animation,
     * the process that loads the image notifies
     * the specified image observer.
     *
     * @param img      the specified image to be drawn. This method does
     *                 nothing if {@code img} is null.
     * @param x        the <i>x</i> coordinate.
     * @param y        the <i>y</i> coordinate.
     * @param bgcolor  the background color to paint under the
     *                 non-opaque portions of the image.
     * @param observer object to be notified as more of
     *                 the image is converted.
     * @return {@code false} if the image pixels are still changing;
     * {@code true} otherwise.
     * @see Image
     * @see ImageObserver
     * @see ImageObserver#imageUpdate(Image, int, int, int, int, int)
     */
    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return wrapped.drawImage(img, x, y, bgcolor, observer);
    }

    /**
     * Draws as much of the specified image as has already been scaled
     * to fit inside the specified rectangle.
     * <p>
     * The image is drawn inside the specified rectangle of this
     * graphics context's coordinate space, and is scaled if
     * necessary. Transparent pixels are drawn in the specified
     * background color.
     * This operation is equivalent to filling a rectangle of the
     * width and height of the specified image with the given color and then
     * drawing the image on top of it, but possibly more efficient.
     * <p>
     * This method returns immediately in all cases, even if the
     * entire image has not yet been scaled, dithered, and converted
     * for the current output device.
     * If the current output representation is not yet complete then
     * {@code drawImage} returns {@code false}. As more of
     * the image becomes available, the process that loads the image notifies
     * the specified image observer.
     * <p>
     * A scaled version of an image will not necessarily be
     * available immediately just because an unscaled version of the
     * image has been constructed for this output device.  Each size of
     * the image may be cached separately and generated from the original
     * data in a separate image production sequence.
     *
     * @param img      the specified image to be drawn. This method does
     *                 nothing if {@code img} is null.
     * @param x        the <i>x</i> coordinate.
     * @param y        the <i>y</i> coordinate.
     * @param width    the width of the rectangle.
     * @param height   the height of the rectangle.
     * @param bgcolor  the background color to paint under the
     *                 non-opaque portions of the image.
     * @param observer object to be notified as more of
     *                 the image is converted.
     * @return {@code false} if the image pixels are still changing;
     * {@code true} otherwise.
     * @see Image
     * @see ImageObserver
     * @see ImageObserver#imageUpdate(Image, int, int, int, int, int)
     */
    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return wrapped.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    /**
     * Draws as much of the specified area of the specified image as is
     * currently available, scaling it on the fly to fit inside the
     * specified area of the destination drawable surface. Transparent pixels
     * do not affect whatever pixels are already there.
     * <p>
     * This method returns immediately in all cases, even if the
     * image area to be drawn has not yet been scaled, dithered, and converted
     * for the current output device.
     * If the current output representation is not yet complete then
     * {@code drawImage} returns {@code false}. As more of
     * the image becomes available, the process that loads the image notifies
     * the specified image observer.
     * <p>
     * This method always uses the unscaled version of the image
     * to render the scaled rectangle and performs the required
     * scaling on the fly. It does not use a cached, scaled version
     * of the image for this operation. Scaling of the image from source
     * to destination is performed such that the first coordinate
     * of the source rectangle is mapped to the first coordinate of
     * the destination rectangle, and the second source coordinate is
     * mapped to the second destination coordinate. The subimage is
     * scaled and flipped as needed to preserve those mappings.
     *
     * @param img      the specified image to be drawn. This method does
     *                 nothing if {@code img} is null.
     * @param dx1      the <i>x</i> coordinate of the first corner of the
     *                 destination rectangle.
     * @param dy1      the <i>y</i> coordinate of the first corner of the
     *                 destination rectangle.
     * @param dx2      the <i>x</i> coordinate of the second corner of the
     *                 destination rectangle.
     * @param dy2      the <i>y</i> coordinate of the second corner of the
     *                 destination rectangle.
     * @param sx1      the <i>x</i> coordinate of the first corner of the
     *                 source rectangle.
     * @param sy1      the <i>y</i> coordinate of the first corner of the
     *                 source rectangle.
     * @param sx2      the <i>x</i> coordinate of the second corner of the
     *                 source rectangle.
     * @param sy2      the <i>y</i> coordinate of the second corner of the
     *                 source rectangle.
     * @param observer object to be notified as more of the image is
     *                 scaled and converted.
     * @return {@code false} if the image pixels are still changing;
     * {@code true} otherwise.
     * @see Image
     * @see ImageObserver
     * @see ImageObserver#imageUpdate(Image, int, int, int, int, int)
     * @since 1.1
     */
    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return wrapped.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    /**
     * Draws as much of the specified area of the specified image as is
     * currently available, scaling it on the fly to fit inside the
     * specified area of the destination drawable surface.
     * <p>
     * Transparent pixels are drawn in the specified background color.
     * This operation is equivalent to filling a rectangle of the
     * width and height of the specified image with the given color and then
     * drawing the image on top of it, but possibly more efficient.
     * <p>
     * This method returns immediately in all cases, even if the
     * image area to be drawn has not yet been scaled, dithered, and converted
     * for the current output device.
     * If the current output representation is not yet complete then
     * {@code drawImage} returns {@code false}. As more of
     * the image becomes available, the process that loads the image notifies
     * the specified image observer.
     * <p>
     * This method always uses the unscaled version of the image
     * to render the scaled rectangle and performs the required
     * scaling on the fly. It does not use a cached, scaled version
     * of the image for this operation. Scaling of the image from source
     * to destination is performed such that the first coordinate
     * of the source rectangle is mapped to the first coordinate of
     * the destination rectangle, and the second source coordinate is
     * mapped to the second destination coordinate. The subimage is
     * scaled and flipped as needed to preserve those mappings.
     *
     * @param img      the specified image to be drawn. This method does
     *                 nothing if {@code img} is null.
     * @param dx1      the <i>x</i> coordinate of the first corner of the
     *                 destination rectangle.
     * @param dy1      the <i>y</i> coordinate of the first corner of the
     *                 destination rectangle.
     * @param dx2      the <i>x</i> coordinate of the second corner of the
     *                 destination rectangle.
     * @param dy2      the <i>y</i> coordinate of the second corner of the
     *                 destination rectangle.
     * @param sx1      the <i>x</i> coordinate of the first corner of the
     *                 source rectangle.
     * @param sy1      the <i>y</i> coordinate of the first corner of the
     *                 source rectangle.
     * @param sx2      the <i>x</i> coordinate of the second corner of the
     *                 source rectangle.
     * @param sy2      the <i>y</i> coordinate of the second corner of the
     *                 source rectangle.
     * @param bgcolor  the background color to paint under the
     *                 non-opaque portions of the image.
     * @param observer object to be notified as more of the image is
     *                 scaled and converted.
     * @return {@code false} if the image pixels are still changing;
     * {@code true} otherwise.
     * @see Image
     * @see ImageObserver
     * @see ImageObserver#imageUpdate(Image, int, int, int, int, int)
     * @since 1.1
     */
    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return wrapped.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    /**
     * Disposes of this graphics context and releases
     * any system resources that it is using.
     * A {@code Graphics} object cannot be used after
     * {@code dispose} has been called.
     * <p>
     * When a Java program runs, a large number of {@code Graphics}
     * objects can be created within a short time frame.
     * Although the finalization process of the garbage collector
     * also disposes of the same system resources, it is preferable
     * to manually free the associated resources by calling this
     * method rather than to rely on a finalization process which
     * may not run to completion for a long period of time.
     * <p>
     * Graphics objects which are provided as arguments to the
     * {@code paint} and {@code update} methods
     * of components are automatically released by the system when
     * those methods return. For efficiency, programmers should
     * call {@code dispose} when finished using
     * a {@code Graphics} object only if it was created
     * directly from a component or another {@code Graphics} object.
     *
     * @see Graphics#//finalize
     * @see Component#paint
     * @see Component#update
     * @see Component#getGraphics
     * @see Graphics#create
     */
    @Override
    public void dispose() {
        wrapped.dispose();
    }

    /**
     * Returns a {@code String} object representing this
     * {@code Graphics} object's value.
     *
     * @return a string representation of this graphics context.
     */
    @Override
    public String toString() {
        return wrapped.toString();
    }

    /**
     * Returns the bounding rectangle of the current clipping area.
     *
     * @return the bounding rectangle of the current clipping area
     * or {@code null} if no clip is set.
     * @deprecated As of JDK version 1.1,
     * replaced by {@code getClipBounds()}.
     */
    @Deprecated
    @Override
    public Rectangle getClipRect() {
        return wrapped.getClipRect();
    }

    /**
     * Returns true if the specified rectangular area might intersect
     * the current clipping area.
     * The coordinates of the specified rectangular area are in the
     * user coordinate space and are relative to the coordinate
     * system origin of this graphics context.
     * This method may use an algorithm that calculates a result quickly
     * but which sometimes might return true even if the specified
     * rectangular area does not intersect the clipping area.
     * The specific algorithm employed may thus trade off accuracy for
     * speed, but it will never return false unless it can guarantee
     * that the specified rectangular area does not intersect the
     * current clipping area.
     * The clipping area used by this method can represent the
     * intersection of the user clip as specified through the clip
     * methods of this graphics context as well as the clipping
     * associated with the device or image bounds and window visibility.
     *
     * @param x      the x coordinate of the rectangle to test against the clip
     * @param y      the y coordinate of the rectangle to test against the clip
     * @param width  the width of the rectangle to test against the clip
     * @param height the height of the rectangle to test against the clip
     * @return {@code true} if the specified rectangle intersects
     * the bounds of the current clip; {@code false}
     * otherwise.
     */
    @Override
    public boolean hitClip(int x, int y, int width, int height) {
        return wrapped.hitClip(x, y, width, height);
    }

    /**
     * Returns the bounding rectangle of the current clipping area.
     * The coordinates in the rectangle are relative to the coordinate
     * system origin of this graphics context.  This method differs
     * from {@link #getClipBounds() getClipBounds} in that an existing
     * rectangle is used instead of allocating a new one.
     * This method refers to the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * If no clip has previously been set, or if the clip has been
     * cleared using {@code setClip(null)}, this method returns the
     * specified {@code Rectangle}.
     *
     * @param r the rectangle where the current clipping area is
     *          copied to.  Any current values in this rectangle are
     *          overwritten.
     * @return the bounding rectangle of the current clipping area.
     */
    @Override
    public Rectangle getClipBounds(Rectangle r) {
        return wrapped.getClipBounds(r);
    }
}