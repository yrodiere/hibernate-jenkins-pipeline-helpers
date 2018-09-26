/*
 * Hibernate Helpers for Jenkins pipelines
 *
 * License: Apache License, version 2 or later.
 * See the LICENSE.txt file in the root directory or <https://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.jenkins.pipeline.helpers.job.configuration

import groovy.transform.PackageScope
import groovy.transform.PackageScopeTarget
import org.hibernate.jenkins.pipeline.helpers.scm.ScmSource
import org.hibernate.jenkins.pipeline.helpers.util.DslUtils

@PackageScope([PackageScopeTarget.CONSTRUCTORS, PackageScopeTarget.FIELDS, PackageScopeTarget.METHODS])
class JobConfiguration {
	final JdkConfiguration jdk
	final MavenConfiguration maven
	final JobTrackingConfiguration tracking

	private def file

	public JobConfiguration(def script, ScmSource scmSource) {
		this.jdk = new JdkConfiguration()
		this.maven = new MavenConfiguration(script)
		this.tracking = new JobTrackingConfiguration(script, scmSource)
	}

	public def getFile() {
		file
	}

	public void setup(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = DSLElement) Closure closure) {
		DSLElement dslElement = new DSLElement(this)
		DslUtils.delegateTo(Closure.DELEGATE_FIRST, dslElement, closure)

		jdk.complete()
		maven.complete()
		tracking.complete(file)
	}

	/*
	 * WARNING: this class must be static, because inner classes don't work well in Jenkins.
	 * "Qualified this" in particular doesn't work.
	 */
	@PackageScope([PackageScopeTarget.CONSTRUCTORS])
	public static class DSLElement {
		private final JobConfiguration configuration

		private DSLElement(JobConfiguration configuration) {
			this.configuration = configuration
		}

		void file(def file) {
			configuration.file = file
		}

		void jdk(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = JdkConfiguration.DSLElement) Closure closure) {
			DslUtils.delegateTo(Closure.DELEGATE_FIRST, configuration.jdk.dsl(), closure)
		}

		void maven(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MavenConfiguration.DSLElement) Closure closure) {
			DslUtils.delegateTo(Closure.DELEGATE_FIRST, configuration.maven.dsl(), closure)
		}
	}
}
