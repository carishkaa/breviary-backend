/**
 * Props constants.
 */
object Props {
    val author = PropertyEntry.create(propertyName = "author", environmentName = "AUTHOR")

    val codeCoverageMinimum = PropertyEntry.create(propertyName = "code.coverage.min", environmentName = "CODE_COVERAGE_MIN")

    val branchName = PropertyEntry.create(propertyName = "git.branch", environmentName = "GIT_BRANCH")
}
