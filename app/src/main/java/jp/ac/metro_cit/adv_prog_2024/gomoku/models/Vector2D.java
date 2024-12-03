package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

/**
 * Represents a two-dimensional vector or point with integer coordinates. This class is immutable,
 * meaning its x and y values cannot be changed once set.
 */
public class Vector2D {
  /** The x-coordinate of the vector. */
  public final int x;

  /** The y-coordinate of the vector. */
  public final int y;

  /**
   * Constructs a new {@code Vector2D} instance with the specified coordinates.
   *
   * @param x the x-coordinate of the vector
   * @param y the y-coordinate of the vector
   */
  public Vector2D(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
