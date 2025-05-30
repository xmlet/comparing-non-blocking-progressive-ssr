[#ftl]
[#import "includes.ftl" as example/]
<!DOCTYPE html>
<html lang="en-US">
[@example.head/]
<body>
<div class="container">
    [@example.pageTitle/]
    [#list presentations as presentation]
        <div class="card mb-3 shadow-sm rounded">
            <div class="card-header">
                <h5 class="card-title">${presentation.getTitle()} - ${presentation.getSpeakerName()}</h5>
            </div>
            <div class="card-body">
                ${presentation.getSummary()}
            </div>
        </div>
    [/#list]
</div>
</body>
</html>