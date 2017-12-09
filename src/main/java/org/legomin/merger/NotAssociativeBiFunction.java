package org.legomin.merger;

import java.util.function.BiFunction;

/**
 * Marker functional interface for not associative BiFunctions
 */
interface NotAssociativeBiFunction<T> extends BiFunction<T, T, T> {

}
