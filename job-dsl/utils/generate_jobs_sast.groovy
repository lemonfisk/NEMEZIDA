import javaposse.jobdsl.dsl.Job

PRODUCT_CONFIG.each {product, config ->
    def rootDir = config.rootDir

    config.components.each { componentItem ->
        def jobName = "${rootDir}/${componentItem.name}"
        pipelineJob(jobName) {
            parameters {
                stringParam('PRODUCT_NAME', product)
                stringParam('PROJECT_NAME', componentItem.name)
                stringParam('PROJECT_VERSION', config.product_version)
                stringParam('GIT_URL', componentItem.params.git_url ?: config.product_git_url)
                stringParam('PROJECT_GIT_BRANCH', componentItem.params.project_git_branch)
                stringParam('PTAI_LANGUAGE', componentItem.params.ptai_language)
                stringParam('PTAI_JDK_VERSION', componentItem.params.ptai_jdk_version ?: "")
                stringParam('PTAI_EXCLUDE_FILES_MASK', componentItem.params.ptai_exclude_files_mask  ?: "")
            }
            definition {
                 cpsScm {
                        scm {
                            git {
                                remote {
                                    url('git@github.com:lemonfisk/NEMEZIDA.git')
                                    credentials('lemonfisk')
                                }
                                branch('develop/1.1.1')
                            }
                        }
                        lightweight(true)
                        scriptPath("pipelines/BuildSAST.groovy")
                 }
            }
        }
    }
}