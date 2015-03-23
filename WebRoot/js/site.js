(function (d) {
    var k = d.scrollTo = function (a, i, e) {
        d(window).scrollTo(a, i, e)
    };
    k.defaults = {axis: 'xy', duration: parseFloat(d.fn.jquery) >= 1.3 ? 0 : 1};
    k.window = function (a) {
        return d(window)._scrollable()
    };
    d.fn._scrollable = function () {
        return this.map(function () {
            var a = this, i = !a.nodeName || d.inArray(a.nodeName.toLowerCase(), ['iframe', '#document', 'html', 'body']) != -1;
            if (!i)return a;
            var e = (a.contentWindow || a).document || a.ownerDocument || a;
            return d.browser.safari || e.compatMode == 'BackCompat' ? e.body : e.documentElement
        })
    };
    d.fn.scrollTo = function (n, j, b) {
        if (typeof j == 'object') {
            b = j;
            j = 0
        }
        if (typeof b == 'function')b = {onAfter: b};
        if (n == 'max')n = 9e9;
        b = d.extend({}, k.defaults, b);
        j = j || b.speed || b.duration;
        b.queue = b.queue && b.axis.length > 1;
        if (b.queue)j /= 2;
        b.offset = p(b.offset);
        b.over = p(b.over);
        return this._scrollable().each(function () {
            var q = this, r = d(q), f = n, s, g = {}, u = r.is('html,body');
            switch (typeof f) {
                case'number':
                case'string':
                    if (/^([+-]=)?\d+(\.\d+)?(px|%)?$/.test(f)) {
                        f = p(f);
                        break
                    }
                    f = d(f, this);
                case'object':
                    if (f.is || f.style)s = (f = d(f)).offset()
            }
            d.each(b.axis.split(''), function (a, i) {
                var e = i == 'x' ? 'Left' : 'Top', h = e.toLowerCase(), c = 'scroll' + e, l = q[c], m = k.max(q, i);
                if (s) {
                    g[c] = s[h] + (u ? 0 : l - r.offset()[h]);
                    if (b.margin) {
                        g[c] -= parseInt(f.css('margin' + e)) || 0;
                        g[c] -= parseInt(f.css('border' + e + 'Width')) || 0
                    }
                    g[c] += b.offset[h] || 0;
                    if (b.over[h])g[c] += f[i == 'x' ? 'width' : 'height']() * b.over[h]
                } else {
                    var o = f[h];
                    g[c] = o.slice && o.slice(-1) == '%' ? parseFloat(o) / 100 * m : o
                }
                if (/^\d+$/.test(g[c]))g[c] = g[c] <= 0 ? 0 : Math.min(g[c], m);
                if (!a && b.queue) {
                    if (l != g[c])t(b.onAfterFirst);
                    delete g[c]
                }
            });
            t(b.onAfter);
            function t(a) {
                r.animate(g, j, b.easing, a && function () {
                    a.call(this, n, b)
                })
            }
        }).end()
    };
    k.max = function (a, i) {
        var e = i == 'x' ? 'Width' : 'Height', h = 'scroll' + e;
        if (!d(a).is('html,body'))return a[h] - d(a)[e.toLowerCase()]();
        var c = 'client' + e, l = a.ownerDocument.documentElement, m = a.ownerDocument.body;
        return Math.max(l[h], m[h]) - Math.min(l[c], m[c])
    };
    function p(a) {
        return typeof a == 'object' ? a : {top: a, left: a}
    }
})(jQuery);


(function ($) {

    $(function () {

        /* toggle links */
        $('.toggle-link').click(function (e) {

            var target = $($(this).attr('href')).toggleClass('hidden');

            $.scrollTo(target);

            e.preventDefault();

        });

    });

})(this.jQuery);


$(document).ready(function () {
    var topicID;


    //发送私信
    $("#send_msg").on("click", function () {
        $("#send_msg").hide();
        $("#text_send").fadeOut("fast");
        $("#send_loading").show();
        $.post(base + "/msg/sendMsg", {content: $("#text_send").val(), toID: $("#user_id").val()}, function (data) {
            if (data.msg == '1') {
                $("#send_loading").text("发送成功");
            } else {
                $("#send_loading").addClass("text-danger").text(data.msg);
            }
        }).error(function () {
            $("#send_loading").addClass("text-danger").text("网络异常");
        });
    });

    $("#send_launch").on("click", function () {
        $("#send_msg").show();
        $("#text_send").show();
        $("#send_loading").removeClass("text-danger").text("加载中").hide();
    });


    //添加新投票
    $("#add_vote").on("click", function () {
        $("#vote_form").fadeOut("fast");
        $.post(base + "/topic/newVote", {
            title: $("#vote_title").val(),
            content: $("#vote_content").val(),
            groupID: $("#groupID").val()
        }, function (data) {
            if (data.msg == '1') {
                topicID = data.voteID;
                $("#choice_form").fadeIn("fast");
            } else {
                $("#alert-title").text("输入有误");
                $("#alert-title").fadeIn("fast");
                $("#vote_form").fadeIn("fast");
            }
        }).error(function () {
            $("#alert-title").text("网络异常");
            $("#alert-title").fadeIn("fast");
        });
    });

    //添加新选项(与上面相关)
    $("#add_choice").on("click", function () {
        $("#alert-title").fadeOut("fast");
        $.post(base + "/topic/newChoice", {voteID: topicID, title: $("#choice").val()}, function (data) {
            if (data.num > '0')
                $("#vote-success").fadeIn("fast");
            if (data.msg == '1') {
                var input = "<div class='alert alert-success' role='alert'>" + $("#choice").val() + "</div>";
                $("#choice").val("");
                $("#choice0").append(input);
            } else if (data.msg == '0') {
                $("#alert-title").text("已达上限");
                $("#alert-title").fadeIn("fast");
            } else {
                $("#alert-title").text("输入有误");
                $("#alert-title").fadeIn("fast");
            }
        }).error(function () {
            $("#alert-title").text("网络异常");
            $("#alert-title").fadeIn("fast");
        });
    });

    //参加教师组
    $("#join").on("click", function () {
        $("#pw").removeClass("alert-danger");
        $.post(base + "/user/joinTeacherGroup", {groupID: a, pw: $("#pw").val()}, function (data) {
            if (data.msg == '1') {
                parent.document.location.href = base + "/groups";
            } else {
                $("#pw").addClass("alert-danger").val("");
                $("#pw").attr("placeholder", "密码错误");
            }
        }).error(function () {
            $("#pw").addClass("alert-danger").val("");
            $("#pw").attr("placeholder", "网络异常");
        });
    });

    //参加学生组
    $("#join0").on("click", function () {
        $("#pw0").removeClass("alert-danger");
        $.post(base + "/user/joinGroup", {groupID: a, pw: $("#pw0").val()}, function (data) {
            if (data.msg == '1') {
                parent.document.location.href = base + data.url;
            } else if (data.msg == '2') {
                $("#pw0").addClass("alert-danger").val("");
                $("#pw0").attr("placeholder", "你已加入该组");
            } else if (data.msg == '3') {
                $("#pw0").addClass("alert-danger").val("");
                $("#pw0").attr("placeholder", "你的组已经到达上线");
            } else {
                $("#pw0").addClass("alert-danger").val("");
                $("#pw0").attr("placeholder", "密码错误");
            }
        }).error(function () {
            $("#pw0").addClass("alert-danger").val("");
            $("#pw0").attr("placeholder", "网络异常");
        });
    });

});

