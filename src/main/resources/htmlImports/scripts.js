function startTabs(){

	$(function(){
        $('ul.tabs li:first').addClass('active');
        $('.block article').hide();
        $('.block article:first').show();
        $('ul.tabs li').on('click',function(){
            $('ul.tabs li').removeClass('active');
            $(this).addClass('active')
            $('.block article').hide();
            var activeTab = $(this).find('a').attr('href');
            $(activeTab).show();
            return false;
        });
	});
}

function hideUnavailbleProducts(){
    $(".unavailable").hide();

    $("#tab1 > table").each(function(){
        var table = $(this);

        if( table.find("tbody > tr").filter(".available").length == 0 ){
            table.hide();
        }
    });
}