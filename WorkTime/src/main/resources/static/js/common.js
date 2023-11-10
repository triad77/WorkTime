$(function () {
    headerEvt();
    gnbSubdepth();
    allChecker();
    headerScroll();
    uiDropdown('.dropdown');
    modalToggle();
    classToggle();
    moreMenu();
    writableGroup();
    categoryToggle();
    toTop('.floating-menu .totop');
    $(this).find('.nice-select').niceSelect(); // selectbox
    fileUpload(); // fileupload
    // likeButton();
    toggleCollapse();
    dataSorting();
    videoModal();
    uiRadioTab();
    activeHeader();
    fileRows();
    // lazyLoading();
});

$(window).on('load', function(){
    xScroll('.sub-depth-wrap .scroll');
    xScroll('.bar-tab-wraps .scroll');
});



// multi file upload Row ADD/DELETE
function fileRows() {
    var fileRow = 
        '<div class="d-flex align-items-center file-wrap mt-1">' +
            '<input class="form-control" type="file" id="" name="">' +
            '<button type="button" class="btn btn-danger" data-toggle="row-delete">삭제</button>' +
        '</div>';

    $(document).on('click', '[data-toggle="row-add"]', function(){
        var add = $(this),
            rows = add.parent('.file-wrap'),
            rowSibling = rows.siblings().length,
            wrap = rows.parent('.multi-file-wrap'),
            max = wrap.data('max-count');

        if ( rowSibling + 1 > max - 1 ) {
            alert('최대 '+max+'개까지 첨부 가능합니다.');
            return;
        }

        wrap.append(fileRow);
    });
    $(document).on('click', '[data-toggle="row-delete"]', function(){
        var btn = $(this),
            rows = btn.parent('.file-wrap');

        rows.remove();
    });
}

function calendar() {

    // 날짜 정보 가져오기
    var date = new Date(); // 현재 날짜(로컬 기준) 가져오기
    var utc = date.getTime() + (date.getTimezoneOffset() * 60 * 1000); // uct 표준시 도출
    var kstGap = 9 * 60 * 60 * 1000; // 한국 kst 기준시간 더하기
    var today = new Date(utc + kstGap); // 한국 시간으로 date 객체 만들기(오늘)
  
    var thisMonth = new Date(today.getFullYear(), today.getMonth(), today.getDate());
    // 달력에서 표기하는 날짜 객체
  
    
    var currentYear = thisMonth.getFullYear(); // 달력에서 표기하는 연
    var currentMonth = thisMonth.getMonth(); // 달력에서 표기하는 월
    var currentDate = thisMonth.getDate(); // 달력에서 표기하는 일
    let div = document.createElement('div');

    function escapeHtml(div) {
        var div = document.createElement("div");
        div.appendChild(document.createTextNode(str));
        return div.innerHTML;
    }

    // kst 기준 현재시간
    // console.log(thisMonth);

    // 캘린더 렌더링
    renderCalender(thisMonth);

    function renderCalender(thisMonth) {

        // 렌더링을 위한 데이터 정리
        currentYear = thisMonth.getFullYear();
        currentMonth = thisMonth.getMonth();
        currentDate = thisMonth.getDate();

        // 이전 달의 마지막 날 날짜와 요일 구하기
        var startDay = new Date(currentYear, currentMonth, 0);
        var prevDate = startDay.getDate();
        var prevDay = startDay.getDay();

        // 이번 달의 마지막날 날짜와 요일 구하기
        var endDay = new Date(currentYear, currentMonth + 1, 0);
        var nextDate = endDay.getDate();
        var nextDay = endDay.getDay();

        // console.log(prevDate, prevDay, nextDate, nextDay);

        // 현재 월 표기
        $('.year-month').text(currentYear + '.' + (currentMonth + 1));

        // 렌더링 html 요소 생성
        calendar = document.querySelector('.dates')
        calendar.innerHTML = '';
        
        // 지난달
        for (var i = prevDate - prevDay + 1; i <= prevDate; i++) {
            calendar.innerHTML = calendar.innerHTML + '<div class="day prev disable">' + i + '</div>'
        }
        // 이번달
        for (var i = 1; i <= nextDate; i++) {
            calendar.innerHTML = calendar.innerHTML + '<div class="day current">' + i + '</div>'
        }
        // 다음달
        for (var i = 1; i <= (7 - nextDay == 7 ? 0 : 7 - nextDay); i++) {
            calendar.innerHTML = calendar.innerHTML + '<div class="day next disable">' + i + '</div>'
        }

        // 오늘 날짜 표기
        if (today.getMonth() == currentMonth) {
            todayDate = today.getDate();
            var currentMonthDate = document.querySelectorAll('.dates .current');
            
            currentMonthDate[todayDate -1].classList.add('today','checkButton');
            currentMonthDate[todayDate -2].classList.add('attendance');
            currentMonthDate[todayDate -1].append('<a href="#" id="checkButton">출석체크</a>');

            
            document.getElementById("checkButton").addEventListener("click", function() {
                var currentDate = new Date();
                var currentTime = currentDate.getHours() + ":" + currentDate.getMinutes() + ":" + currentDate.getSeconds();
              
                var resultElement = document.getElementById("result");
                
                resultElement.innerHTML = "출석체크 완료 (" + currentTime + ")";

                alert("출석 되었습니다.");
              });
        }
        

    }



    // 이전달로 이동
    $('.go-prev').on('click', function() {
        thisMonth = new Date(currentYear, currentMonth - 1, 1);
        renderCalender(thisMonth);
    });

    // 다음달로 이동
    $('.go-next').on('click', function() {
        thisMonth = new Date(currentYear, currentMonth + 1, 1);
        renderCalender(thisMonth); 
    });

    
}






function likeButton() {
    const heartIcon = document.querySelector(".like-button .heart-icon");
    const likesAmountLabel = document.querySelector(".like-button .likes-amount");

    let likesAmount = 0;

    heartIcon.addEventListener("click", () => {
    heartIcon.classList.toggle("liked");
    if (heartIcon.classList.contains("liked")) {
        likesAmount++;
    } else {
        likesAmount--;
    }
    likesAmountLabel.innerHTML = likesAmount;
    });
}



function passEye() {
  
  var x = document.getElementById("userPWD");
  if (x.type === "password") {
    x.type = "text";
  } else {
    x.type = "password";
  }
}

function passEye2() {
    var x = document.getElementById("userPWD2");
    if (x.type === "password") {
      x.type = "text";
    } else {
      x.type = "password";
    }
}

/*activeHeader*/
function activeHeader() {
    document.querySelectorAll('.dep2').forEach(link => {
        if(link.href === window.location.href){  
        var par = link.closest("ul").parentElement;
        console.log(par);
        console.log("클릭한 대상의 텍스트"+link.innerText);
        console.log("클릭한 대상의 부모 ul의 부모 li" + par)
        par.setAttribute('aria-current', 'page')

        /*addClass('active'); activeHeader
        removeclass ('active') 조건을 한번더 넣는거보다 
        setAttribute('aria-current', 'page') 클릭한 부모에 해당 속성을 넣는게 더 편함*/

        }
    })
    
}


    
    




//lazyLoading
// function lazyLoading(){
//     var loading_img = $(".poster").find("img").addClass("swiper-lazy");
//     loading_img.parent().append("<div class='swiper-lazy-preloader'></div>")
//     for(var i=0; i<4; i++){
//         swiper[0].lazy.loadInSlide(i)
//     };
// }
    
    

function headerEvt() {
    // pc
    $('#gnb a.dep1').mouseenter(function(){
        $('header').addClass('open');
    });
    $('header').mouseleave(function(){
        $('header').removeClass('open');
    });

    // mobile
    $('header .btn-menu-all').click(function(){
        var nav = $('#gnb');
        if ($(this).hasClass('open')){
            nav.removeClass('m-open');
            $('header .header-dim').removeClass('m-open');
            setTimeout(function(){
                // nav.hide();
                $('header .header-dim').remove();
            }, 320);
            $(this).removeClass('open');
        } else {
            nav.before('<div class="header-dim"></div>');
            nav.show();
            setTimeout(function(){
                nav.addClass('m-open');
                $('header .header-dim').addClass('m-open');
            }, 50);
            $(this).addClass('open');
        }
    });


    // mobile gnb toggle
    $('#gnb .btn-depth-more').click(function(){
        var btn = $(this);
        var par = $('#main_gnb > li');
        var parli = btn.closest("li");
        var dep2 = btn.siblings('ul.depth2');
        var allBtn = $('#gnb .btn-depth-more');
        var allDep2 = $('#gnb ul.depth2');

        if (btn.hasClass('open')){
            dep2.slideUp();
            parli.removeClass('on')
            btn.removeClass('open');
        } else {
            par.removeClass('on')
            allBtn.removeClass('open');
            allDep2.slideUp();
            parli.addClass('on')
            btn.addClass('open');
            dep2.slideDown();
        }
    });
}


// floating menu(main only)
function floatingDip() {
    var didScroll,
        lastScrollTop = 0,
        delta = 5,
        oh = $(window).outerHeight();

    if ( $('.floating-menu').length <= 0 ) return;

    $(window).scroll(function (e) {
        didScroll = true;
    });
    setInterval(function () {
        if (didScroll) {
            hasScrolled();
            didScroll = false;
        }
    }, 150);

    function hasScrolled() {
        var st = $(this).scrollTop();
        if (Math.abs(lastScrollTop - st) <= delta)
            return;

        if (st > lastScrollTop && st > oh) {
            $('.floating-menu').addClass('on');
        } else if (st < lastScrollTop && st < oh) {
            $('.floating-menu').removeClass('on');
        }

        lastScrollTop = st;
    }
}

// header event
function headerScroll() {
    var didScroll,
        lastScrollTop = 0,
        delta = 5,
        navbarHeight = $('header').outerHeight();

    $(window).scroll(function (e) {
        didScroll = true;
    });
    
    setInterval(function () {
        if (didScroll) {
            hasScrolled();
            didScroll = false;
        }
    }, 150);

    function hasScrolled() {
        var st = $(this).scrollTop();
        if (Math.abs(lastScrollTop - st) <= delta)
            return;

        if (st > lastScrollTop) {
            $('header').addClass('scroll');
        } else if (st < lastScrollTop) {
            $('header').removeClass('scroll');
        }

        lastScrollTop = st;
    }
}

function gnbSubdepth() {
    var depth1 = $('header .head-top .menu-all'),
        header = $('header');

    depth1.on('click', function(){
        if ($(this).hasClass('open')) {
            header.removeClass('open');
            $(this).removeClass('open');
        } else {
            header.addClass('open');
            $(this).addClass('open');
        }
    });

    $('#gnb a.dep1').mouseenter(function(){
        $('header').addClass('open');
    });
    $('header').mouseleave(function(){
        $('header').removeClass('open');
    });
    
}



// checkbox all check
function allChecker() {
    var obj = '[data-toggle="allChk"]',
        $obj = $(obj);

    if ($obj.length <= 0) return;
    $obj.each(function(){
        var $input = $(this).find('.chk-all'),
            name = $input.attr('name');

        $input.on('change', function(){
            var $name = $(this).attr('name'),
                $wrapper = $(this).parents(obj),
                $childs = $wrapper.find('input[name='+ $name +']');
            
            if ($(this).prop("checked")) {
                $childs.prop("checked", true);
            } else {
                $childs.prop("checked", false);
            }
        });
        
        $('input[name=' + name + ']').each(function () {
            var $this = $(this);
    
            $this.on('change', function () {
                var total = $('input[name=' + name + ']').length;
                var chked = $('input[name=' + name + ']:checked').not($input).length + 1;
                if (chked == total) {
                    $input.prop("checked", true);
                } else {
                    $input.prop("checked", false);
                }
            });
        });
    });
}



function moreMenu() {
    $('header .dep1-more').click(function() {
        var parent = $(this).parent('li'),
            allLi = parent.siblings('li'),
            allDepth = allLi.children('ul.depth2'),
            allBtn = allLi.children('.dep1-more'),
            depth = $(this).siblings('ul.depth2');

        allDepth.slideUp();
        allBtn.removeClass('open');

        $(this).addClass('open');
        depth.slideDown();
    });
}

function gnbOpen() {
    var header = $('header'),
        gnb = $('header .gnb-all'),
        menuBtn = $('header .btn-menu-all'),
        dim = '<div class="header-dim" onclick="gnbClose()">&nbsp;</div>';
    gnb.before(dim);
    gnb.show();
    scrollPrevent(true);
    setTimeout(function(){
        header.addClass('m-open');
        menuBtn.addClass('open');
    }, 20);
}

function gnbClose() {
    var header = $('header'),
        gnb = $('header .gnb-all'),
        menuBtn = $('header .btn-menu-all');

    $('.header-dim').remove();
    menuBtn.removeClass('open');
    header.removeClass('m-open');
    scrollPrevent(false);
    setTimeout(function () {
        gnb.hide();
    }, 200);
}

function scrollPrevent(prop) {
    if ( prop == true ) {
        $('body').css({'overflow':'hidden'});
    } else {
        $('body').css({'overflow':'initial'});
    }
}

// sub depth 
function subdepthToggle() {


    var $subdepth = $('.sub-depth-wrap'),

        menus = $subdepth.find('.dropdown-menu'),
        button = $subdepth.find('.selected');

    button.on('click', function(e){
        var prt = $(this).parent('.dropdown-menu');

        e.preventDefault();
        if (prt.hasClass('open')) {
            prt.removeClass('open');
        } else {
            menus.removeClass('open');
            prt.addClass('open');
        }
    });

    $(document).on('click', 'html', function(e){
        var prt = $(e.target).parents();
        if (!prt.is($subdepth) ) {
            menus.removeClass('open');
        }
    });

    $(document).on('click', 'html', function(p){
        var par = $(p.target).parents();
        console.log(par);
        // link.setAttribute('aria-current', 'page')
        if (!par.is($subdepth) ) {
            menus.removeClass('open');
        }
    });

}

// totop
function toTop(obj) {
    var $btn = $(obj);

    $btn.click(function(e){
        e.preventDefault();
        $('html, body').animate({
            scrollTop : 0
        }, 400);
        return false;
    });
}

// uiRadioTab
function uiRadioTab() {
    var tab = '[data-evt*="radio-tab"]';
    $(document).on('click', tab + ' .rdo-wrap', function (e) {
        var $this = $(this),
            input = $(this).find('input'),
            id = $this.attr('id'),
            $target = $('[data-id=' + id + ']'),
            $siblings = $this.parents('li').siblings('').find('.rdo-wrap');


        $siblings.each(function () {
            var id = $(this).attr('id');
            $('[data-id=' + id + ']').hide();
            $(this).find('input').prop('checked', false);
        });
        input.prop('checked', true);
        $target.show();

        return false;
    });
}



// iscroll outerwidth
function calcWidth(target) {
    var $target = $(target);

    $target.each(function(){
        var child = $(this).children(),
            width = 0;

        child.each(function(){
            width += $(this).outerWidth(true);
        });
        $(this).css('width', width);
    });
}

// iscroll
function xScroll(obj) {
    var $obj = $(obj),
        tabs = $obj.find('.tabs');

    if ( $(obj).length <= 0 ) {
        return
    } else {
        $(window).resize(function(){
            calcWidth(tabs);
        });
        calcWidth(tabs);
        new IScroll(obj , {
            scrollX : true,
            scrollY : false,
            mouseWheel : false,
            autoCenterScroll : true,
            bounce : true
        });
    }
}



// accordion
function accordion() {
    var wrap = $('.ui-accordion'),
        list = wrap.find('li'),
        title = wrap.find('.accord-title'),
        toggle = title.find('.btn-toggle');

    if(wrap.length <= 0) return;

    // 접근성 대응
    list.each(function(){
        var $btn = $(this).find('.accord-title .btn-toggle'),
            $content = $(this).find('.accord-cont'),
            id = $(this).index();

        $btn.attr({
            'id' : 'accord-toggle' + id,
            'aria-controls' : 'accord-cont' + id
        });
        $content.attr({
            'id' : 'accord-cont' + id,
            'role' : 'region',
            'aria-labelledby' : 'accord-toggle' + id
        })
    });

    toggle.click(function(){
        var li = $(this).parent('.accord-title').parent('li'),
            cont = $(this).parent('.accord-title').siblings('.accord-cont'),
            blind = $(this).find('.blind');

        if (li.hasClass('open')) {
            $(this).attr('aria-expanded', 'false');
            cont.slideUp();
            li.removeClass('open');
            blind.text('상세보기');
        } else {
            $(this).attr('aria-expanded', 'true');
            cont.slideDown();
            li.addClass('open');
            blind.text('닫기');
        }
    });
}

// data sorting
function dataSorting() {
    var tab = '[data-type="sortingTab"]',
        $tab = $(tab),
        btn = $tab.find('a');

    var listWrap = '[data-type="sortingTarget"]',
        $wrap = $(listWrap),
        listAll = $wrap.find('li');

    if($tab.length <= 0) return;

    btn.click(function(e){
        var num = $(this).data('category-num'),
            $target = $('[data-category-id='+num+']');

        e.preventDefault();
        $(this).parent('li').siblings().removeClass('active');
        $(this).parent('li').addClass('active');
        listAll.hide();

        var empty = '<li class="empty"><p class="nodata">게시글이 없습니다.</p></li>',
            uls = $wrap.find('.lists');

        if (num === 'all') {
            uls.find('li.empty').remove();
            listAll.show();
        } else {
            if ($target.length <= 0) {
                uls.find('li.empty').remove();
                uls.append(empty);
            } else {
                uls.find('li.empty').remove();
                $target.show();
            }
        }
    });
}

// class toggle
function classToggle() {
    var classToggle = $('[data-toggle="class-toggle"]');

    if (classToggle.length <= 0) return;

    var btns = classToggle.find('button, a');
    
    btns.on('click', function(){
        btns.removeClass('active');
        $(this).addClass('active');    
    });
}

// file upload 
function fileUpload() {
    var obj = $('.inputfile-wrap');
    
    if (obj.length <= 0) return;

    var fileTarget = obj.find('input[type=file]');

    fileTarget.on('change', function(){
        if (window.FileReader) {
            var filename = $(this)[0].files[0].name;
        } else {
            var filename = $(this).val().split('/').pop().split('\\').pop();
        }

        $(this).siblings('input[type=text]').val(filename);
    });
}

// dropdown
function uiDropdown(obj) { 
    function classToggle(target) {
        if ( target.hasClass('open') ) {
            target.removeClass('open');
        } else {
            target.addClass('open');
        }
    }
    function determineDropDirection(target){
        var $target = $(target);
        $target.css({
            visibility: "hidden",
            display: "block"
        });
        $target.parent().removeClass("dropup");
    
        if ($target.offset().top + $target.outerHeight() > $(window).innerHeight() + $(window).scrollTop()){
            $target.parent().addClass("dropup");
        }
        $target.removeAttr("style");
    }

    var wrap = $(obj),
        ul = wrap.find('ul.lists');
    ul.each(function(){
        var self = $(this);
        determineDropDirection(self);
        $(window).scroll(function(){
            determineDropDirection(self);
        });
    });

    wrap.each(function(){
        var $wrap = $(this);
            btns = $wrap.find('.btn-toggle'),
            lists = $wrap.find('.lists');

        btns.click(function(e){
            e.stopPropagation();
            e.preventDefault();

            wrap.removeClass('open');
            classToggle($wrap);
        });
        lists.click(function(e){
            classToggle($wrap);
        });
        $('html').click(function(e){
            if ( !$(e.target).is($wrap) ) {
                $wrap.removeClass('open');
            }
        });
    });
}

// tab
function uiTab() {
    var tab = '[data-evt="tab"]';
    $(document).on('click', tab + ' a', function (e) {
        e.preventDefault();

        var $this = $(this),
            id = $this.attr('href'),
            $target = $('[data-id=' + id + ']'),
            $siblings = $this.parents('li').siblings('').find('a');

        $siblings.each(function () {
            var id = $(this).attr('href');
            $(this).parents('li').removeClass('active');
            $('[data-id=' + id + ']').hide();
        });
        $this.parents('li').addClass('active');
        $target.show();

        return false;
    });

    if ( tab.length <= 0 ) return;
    $(tab).find('a').each(function(){
        var tg = $(this).attr('href');

        $('[data-id="'+tg+'"]').hide();
    });
    $(tab + ' > li:first-child a').click();
}

function mapChange() {
    // map click
    $(document).on('click', '.map-select .spot a.pin', function(e){
        var self = $(this),
            myNum = self.data('spot'),
            mySpot = self.parent('.spot'),
            spots = $('.map-select .spot');

        var tab = $('[data-toggle="mapChange"]'),
            lis = tab.find('li');

        e.preventDefault();
        spots.removeClass('active');
        mySpot.addClass('active');

        lis.removeClass('active');
        tab.find('[data-num="'+myNum+'"]').addClass('active');

        mapdataSwitch(myNum);
    });

    // tab click
    $(document).on('click', '[data-toggle="mapChange"] a', function(e){
        var self = $(this),
            myLi = self.parent('li'),
            myNum = myLi.data('num'),
            allLi = $('[data-toggle="mapChange"] li');

        var selects = $('.map-select'),
            spots = selects.find('div.spot');

        e.preventDefault();
        allLi.removeClass('active');
        myLi.addClass('active');

        spots.removeClass('active');
        selects.find('[data-spot="'+myNum+'"]').parent('.spot').addClass('active');

        mapdataSwitch(myNum);
    });

    $('.map-select .spot.spot01 a').click();
}

function mapdataSwitch(val) {
    var timestamp = "";
    var key = "";
    switch(val) {
        case "01" : // 강남
            timestamp = "1623027159997";
            key = "2647r";
            break;
        case "02" : // 영등포
            timestamp = "1623027269147";
            key = "2647x";
            break;
        case "03" : // 평택
            timestamp = "1623027527603";
            key = "26482";
            break;
        case "04" : // 천안
            timestamp = "1623027557419";
            key = "26483";
            break;
    }
    $('[data-toggle="mapSwitch"]').html('');
    $('[data-toggle="mapSwitch"]').attr('class', '');
    $('[data-toggle="mapSwitch"]').addClass('root_daum_roughmap root_daum_roughmap_landing');
    $('[data-toggle="mapSwitch"]').attr('id', 'daumRoughmapContainer'+timestamp);
    new daum.roughmap.Lander({
        "timestamp" : timestamp,
        "key" : key,
        "mapWidth" : "640",
        "mapHeight" : "360"
    }).render();
}

// modal 
function modalToggle() {
    var modalToggle = $('[data-toggle="modal"]'),
        modalClose = $('[data-toggle="modal-close"]');

    if (modalToggle.length <= 0) return;

    modalToggle.on('click', function(){
        var modalTarget = $(this).data('target') || $(this).attr('href');
            modal = $(modalTarget);

        modal.removeAttr('aria-hidden');
        modal.attr('aria-modal', true);
        modal.show();
    });

    modalClose.on('click', function(){
        var modal = $(this).parents('.modal');

        modal.hide();
        modal.removeAttr('aria-modal');
        modal.attr('aria-hidden', true);
    });
}

// openWinodwPopup
function openWindowPop(url, name) {
    var options =  'top=10, left=10, width=1200, height=600, status=no, menubar=no, toolbar=no, resizable=no';
    window.open(url, name, options);
}




// 홍보자료관리 collapse
function toggleCollapse() {
    var tg = $('[data-evt="tg-toggle-wrap"]');

    if( tg.length <= 0 ) return;
    tg.find('.toggle-tg').hide();

    $('[data-toggle="tg-toggle"]').on('click', function(){
        var self = $(this),
            target = self.data('target'),
            wrap = self.parents('[data-evt="tg-toggle-wrap"]'),
            allBtn = wrap.find('[data-toggle="tg-toggle"]'),
            allTarget = wrap.find('.toggle-tg');

        allTarget.hide();
        allBtn.attr('aria-expanded', 'false');

        $(target).show();
        self.attr('aria-expanded', 'true');
    });
}

// input category
function categoryToggle() {
    $('[data-toggle="category-collapse"] input').on('change', function(){
        var self = $(this),
            my = self.attr('data-visible-target'),
            myArr = my? my.split(',') : [],
            targets = $('[data-collapse-num]');

        if (self.is(':checked')) {
            targets.hide();

            var target = '';
            $.each(myArr, function(index, value){
                target = '[data-collapse-num="'+value.trim()+'"]';
                $(target).show();
            });
        } else {
            return;
        }
    });

    $('[data-toggle="category-collapse"] input#cate05').click();
}

// rdo select writable 
function writableGroup() {
    $('[data-toggle="writable"] input[type=radio]').on('change', function(){
        var self = $(this),
            wrap = self.parents('[data-toggle="writable"]'),
            allInput = wrap.find('[data-writable-target]'),
            myInput = self.closest('.rdo-wrap').siblings('[data-writable-target]');

            console.log(allInput);

        if( self.is(':checked') ){
            // 전체 disabled
            $.each(allInput, function(index, value){
                var type = $(this).data('writable-target');
                if( type === 'select' ){
                    $(this).find('select').prop('disabled', true);
                    $(this).find('div.nice-select').addClass('disabled');
                } else if( type === 'input' ){
                    $(this).prop('disabled', true);
                }
            })
            // target writable
            if (myInput.data('writable-target') === 'select'){
                myInput.find('select').prop('disabled', false);
                myInput.find('div.nice-select').removeClass('disabled').addClass('open');
            } else if (myInput.data('writable-target') === 'input'){
                myInput.prop('disabled', false);
                myInput.eq(0).focus();
            }
        }
    });
}

// video modal 
function videoModal(obj) {
    var obj = '[data-control="modal"]',
        $obj = $(obj),
        $target = $('#video'),
        $tit = $target.find('.tit'),
        $desc = $target.find('.desc'),
        $date = $target.find('.date'),
        $src = $target.find('iframe'),
        dim = '<div class="common-dim" aria-hidden="true">&nbsp;</div>';

    $obj.on('click', function(e){
        e.preventDefault();
        var infos = $(this).parents('.items').find('.info'),
            tit = infos.find('.tit').text(),
            desc = infos.find('.desc').text(),
            date = infos.find('.date').html();
            src = infos.find('.src').text();
            option = '?controls=0';

        $tit.text(tit);
        $desc.text(desc);
        $date.html(date);
        $src.attr('src', src+option);

        $target.before(dim);
        $target.show();
    });

    var close = $target.find('.btn-close');

    close.on('click', function(){
        $target.hide();
        $('body').find('.common-dim').remove();

        $tit.text(' ');
        $desc.text(' ');
        $date.html(' ');
        $src.attr('src', ' ');
    });
}