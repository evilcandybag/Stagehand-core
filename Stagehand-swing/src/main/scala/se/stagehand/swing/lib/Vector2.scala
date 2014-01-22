package se.stagehand.swing.lib

import java.awt.Point
import java.awt.Dimension
/**
 * Class for standard vector operations with Integer coordinates.
 */
case class Vector2(x:Int,y:Int) {
  def this(i:Int) = this(i,i)
  
  def +(v: Vector2): Vector2 = Vector2(x + v.x, y + v.y)
  def -(v: Vector2): Vector2 = Vector2(x - v.x, y - v.y)
  def length = Math.sqrt(x ^ 2 + y ^ 2)
  def neg = Vector2(-x,-y)
}

/**
 * Implicit conversions for Vector2, to handle Swingy stuff like it's nuttin, brah!
 */
object Vector2 {
  implicit def fromPoint(p:Point) = Vector2(p.x,p.y)
  implicit def toPoint(v:Vector2) = new Point(v.x,v.y)
  implicit def fromDim(d:Dimension) = Vector2(d.width,d.height)
  implicit def toDim(v:Vector2) = new Dimension(v.x,v.y)
  
  implicit def fromInt(i:Int) = new Vector2(i)
  
  def max(v1: Vector2, v2:Vector2) = if (v1.length >= v2.length) v1; else v2;
}