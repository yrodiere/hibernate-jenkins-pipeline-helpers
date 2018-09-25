/*
 * Hibernate Helpers for Jenkins pipelines
 *
 * License: Apache License, version 2 or later.
 * See the LICENSE.txt file in the root directory or <https://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.jenkins.pipeline.helpers.job.configuration

import groovy.transform.PackageScope
import groovy.transform.PackageScopeTarget

@PackageScope([PackageScopeTarget.CONSTRUCTORS, PackageScopeTarget.FIELDS, PackageScopeTarget.METHODS])
class MavenConfiguration {
	private String localRepositoryPath
	private String defaultTool
	private List<String> producedArtifactPatterns = []

	MavenConfiguration(def script) {
		localRepositoryPath = "$script.env.WORKSPACE/.repository"
	}

	public String getLocalRepositoryPath() {
		return localRepositoryPath
	}

	public String getDefaultTool() {
		defaultTool
	}

	public getProducedArtifactPatterns() {
		return producedArtifactPatterns
	}

	void complete() {
		if (!defaultTool) {
			throw new IllegalStateException("Missing default tool for Maven")
		}
	}

	// Workaround for https://issues.jenkins-ci.org/browse/JENKINS-41896 (which apparently also affects non-static classes)
	DSLElement dsl() {
		return new DSLElement()
	}

	@PackageScope([PackageScopeTarget.CONSTRUCTORS])
	public class DSLElement {
		DSLElement() {
		}

		void defaultTool(String toolName) {
			defaultTool = toolName
		}

		void producedArtifactPattern(String pattern) {
			producedArtifactPatterns.add(pattern)
		}
	}
}
