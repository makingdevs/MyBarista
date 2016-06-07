require 'test_helper'

class CirclesControllerTest < ActionDispatch::IntegrationTest
  test "should get create" do
    get circles_create_url
    assert_response :success
  end

  test "should get show" do
    get circles_show_url
    assert_response :success
  end

end
