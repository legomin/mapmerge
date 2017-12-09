package org.legomin.merger;

import java.util.function.BiFunction;

/**
 * Marker functional interface for associative BiFunctions
 */
interface AssociativeBiFunction<T> extends BiFunction<T, T, T> {

}
