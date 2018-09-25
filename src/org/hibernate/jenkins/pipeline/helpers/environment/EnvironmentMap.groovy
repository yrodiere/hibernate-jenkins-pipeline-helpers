/*
 * Hibernate Helpers for Jenkins pipelines
 *
 * License: Apache License, version 2 or later.
 * See the LICENSE.txt file in the root directory or <https://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.jenkins.pipeline.helpers.environment

class EnvironmentMap {
	final Map<String, List<?>> all

	final Map<String, ?> defaults

	EnvironmentMap(Map<String, List<?>> all) {
		this(all, {it -> it.isDefault()})
	}

	EnvironmentMap(Map<String, List<?>> all, Closure defaultPicker) {
		this.all = all
		// Defaults are set before any user modification (environment removals); this is on purpose.
		all.each { key, envList ->
			defaults.put(key, envList.find(defaultPicker))
		}
	}

	@Override
	String toString() {
		return all.values().flatten().join(', ')
	}

	boolean isEmpty() {
		all.isEmpty() || all.every { key, envList -> envList.isEmpty() }
	}
}
